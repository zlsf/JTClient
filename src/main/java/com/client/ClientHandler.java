package com.client;
/**
 * 客户端回调方法
 * Created by tianjun on 2016/12/19 0019.
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.kj.datacenter.msg.HeartMsg;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import test.Test1;

class ClientHandler extends SimpleChannelInboundHandler {
    private Logger log = LoggerFactory.getLogger(ClientHandler.class);
    private static volatile boolean IsConnect = false;
    private static Object locker = new Object();
    private ExecutorService taskExecutor = Executors.newCachedThreadPool();

    // 客户端连接服务器后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
	System.out.println("客户端连接服务器，开始心跳……");
	synchronized (locker) {
	    IsConnect = true;
	}
	this.startHeartBit(ctx);

    }

    private void startHeartBit(ChannelHandlerContext ctx) {
	taskExecutor.execute(() -> {
	    try {
		HeartMsg bit = new HeartMsg();
		bit.setDeviceId(Test1.c.getDeviceId());
		byte[] req = JSON.toJSONString(bit).getBytes("UTF-8");
		while (IsConnect) {
		    ByteBuf firstMessage = Unpooled.buffer(req.length);
		    firstMessage.writeBytes(req);
		    ctx.writeAndFlush(firstMessage);
		    try {
			Thread.sleep(10000);
		    } catch (Exception ex) {
			continue;
		    }
		}
	    } catch (Exception e) {
		log.error(e.toString());
	    }
	});

    }

    // • 从服务器接收到数据后调用
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {

	System.out.println("client 读取server数据..");
	// 服务端返回消息后
	ByteBuf buf = (ByteBuf) o;
	byte[] req = new byte[buf.readableBytes()];
	buf.readBytes(req);
	String body = new String(req, "UTF-8");
	System.out.println("服务端数据为 :" + body);

    }

    // • 发生异常时被调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	System.out.println("服务端断开连接...");
	synchronized (locker) {
	    IsConnect = false;
	}
	// 释放资源
	ctx.close();
    }

}