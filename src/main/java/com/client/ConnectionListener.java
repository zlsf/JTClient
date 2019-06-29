
package com.client;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

/**
 * 启动的时候 链接不成功 则重连
 * 
 * @author Administrator
 *
 */
public class ConnectionListener implements ChannelFutureListener {

    private DataServer client;

    public ConnectionListener(DataServer client) {
	this.client = client;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
	if (!channelFuture.isSuccess()) {
	    System.out.println("启动不成功 重连");
	    final EventLoop loop = channelFuture.channel().eventLoop();
	    loop.schedule(new Runnable() {

		@Override
		public void run() {
		    client.stop();
		    client.run();
		}
	    }, 20L, TimeUnit.SECONDS);
	}
    }
}
