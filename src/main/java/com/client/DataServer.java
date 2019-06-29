
package com.client;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.config.Config;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public final class DataServer implements Runnable {

    private Logger log = LoggerFactory.getLogger(DataServer.class);

    private ExecutorService serverExecutor = Executors.newSingleThreadExecutor();

    private static volatile boolean running = false;

    /** The future. */
    private Future<?> future;

    private static Object locker = new Object();

    private Config cfg;

    public DataServer(Config cfg) {
	this.cfg = cfg;
	if (!DataServer.running) {
	    start();
	}
    }

    public void start() {
	log.info("开启服务器......");
	synchronized (locker) {
	    future = serverExecutor.submit(this);
	    this.running = true;
	}
	log.info("开启服务结束......");
    }

    /**
     * 停止锟斤拷锟斤拷.
     */
    public void stop() {
	log.info("停止服务......");
	synchronized (locker) {
	    if (future != null) {
		future.cancel(running);
		future = null;
	    }
	    if (null != nioEventLoopGroup) {
		nioEventLoopGroup.shutdownGracefully();
		nioEventLoopGroup = null;
	    }
	    this.running = false;
	}
	log.info("停止服务结束......");
    }

    EventLoopGroup nioEventLoopGroup;

    public void run() {

	Bootstrap bootstrap = new Bootstrap();
	nioEventLoopGroup = new NioEventLoopGroup();
	try {

	    bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class)
		    .remoteAddress(new InetSocketAddress(this.cfg.getIp(), this.cfg.getPort()))
		    .option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
			    ch.pipeline().addLast(new ClientHandler(DataServer.this));
			}
		    });
	    bootstrap.connect().addListener(new ConnectionListener(this));
	    ChannelFuture f = bootstrap.connect();
	    f.channel().closeFuture();
	} catch (Exception ex) {
	    nioEventLoopGroup.shutdownGracefully();
	} finally {
	}
    }

}
