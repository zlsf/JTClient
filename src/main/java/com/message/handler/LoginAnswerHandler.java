package com.message.handler;

import com.alibaba.fastjson.JSON;
import com.client.SessionManager;
import com.kj.datacenter.msg.LoginAnswerMsg;
import com.message.BaseHandler;

public class LoginAnswerHandler extends BaseHandler {

    @Override
    public void dealMessage() {
	LoginAnswerMsg msg = JSON.parseObject(json, LoginAnswerMsg.class);

	if (msg.isFlag()) {
	    SessionManager.getInstance().setLogin(msg.isFlag());
	}
    }

}
