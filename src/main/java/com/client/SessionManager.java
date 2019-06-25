
package com.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
