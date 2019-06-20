
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
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

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
	    this.running = true;
	    future = serverExecutor.submit(this);
	}
	log.info("开启服务结束......");
    }

    /**
     * 停止锟斤拷锟斤拷.
     */
    public void stop() {
	log.info("停止服务......");
	synchronized (locker) {
	    if (!running)
		return;

	    if (future == null)
		return;

	    future.cancel(running);
	    future = null;
	    this.running = false;
	}
	log.info("停止服务结束......");
    }

    public void run() {
	EventLoopGroup nioEventLoopGroup = null;
	try {

	    // 创建Bootstrap对象用来引导启动客户端
	    Bootstrap bootstrap = new Bootstrap();
	    // 创建EventLoopGroup对象并设置到Bootstrap中，EventLoopGroup可以理解为是一个线程池，这个线程池用来处理连接、接受数据、发送数据
	    nioEventLoopGroup = new NioEventLoopGroup();
	    // 创建InetSocketAddress并设置到Bootstrap中，InetSocketAddress是指定连接的服务器地址
	    bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class)
		    .remoteAddress(new InetSocketAddress(this.cfg.getIp(), this.cfg.getPort()))
		    .handler(new ChannelInitializer<SocketChannel>() {
			// 添加一个ChannelHandler，客户端成功连接服务器后就会被执行
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
			    ch.pipeline().addLast(new ClientHandler());
			}
		    });
	    // • 调用Bootstrap.connect()来连接服务器
	    ChannelFuture f = bootstrap.connect().sync();
	    // • 最后关闭EventLoopGroup来释放资源
	    f.channel().closeFuture().sync();
	} catch(Exception ex)
	{
	    
	}
	finally {
	    try {
		nioEventLoopGroup.shutdownGracefully().sync();
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

    private ChannelInitializer<SocketChannel> channelnit = new ChannelInitializer<SocketChannel>() {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
	    channel.pipeline().addLast("logging", new LoggingHandler(LogLevel.TRACE))
		    .addLast("idle-handler", new IdleStateHandler(100, 100, 100))
		    .addLast("client-handler", new ClientHandler());
	}
    };
}
