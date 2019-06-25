
package com.client;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.kj.datacenter.msg.BaseMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;

public class SessionManager {

    /** The Constant log. */
    private static final Logger log = LoggerFactory.getLogger(SessionManager.class);

    public static SessionManager getInstance() {
	return SessionManagerInstance.INSTANCE;
    }

    private static class SessionManagerInstance {

	private static final SessionManager INSTANCE = new SessionManager();
    }

    private ChannelHandlerContext ctx;
    private boolean isLogin = false;

    public ChannelHandlerContext getCtx() {
	return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
	this.ctx = ctx;
    }

    public boolean isLogin() {
	return isLogin;
    }

    public void setLogin(boolean isLogin) {
	this.isLogin = isLogin;
    }

    public void SendMessage(BaseMessage msg) throws Exception {
	if (this.ctx == null)
	    throw new RuntimeException("连接已断开，不能发送信息");
	if (!this.isLogin)
	    throw new RuntimeException("未能登陆，不能发送信息");

	byte[] req = JSON.toJSONString(msg).getBytes("UTF-8");
	this.sendToServer(this .ctx.channel(), req);

    }

    public void sendToServer(Channel channel, byte[] data) throws InterruptedException {
	for (int i = 0; i < 3; i++) {
	    ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(data)).sync();
	    if (future.isSuccess())
		break;
	}
    }

}
