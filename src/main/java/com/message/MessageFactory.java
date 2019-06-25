package com.message;

import java.util.HashMap;
import java.util.Map;

import com.kj.datacenter.core.Constant;
import com.message.handler.LoginAnswerHandler;

public class MessageFactory {

    private final static Object locker = new Object();

    public static MessageFactory getInstance() {
	return MessageFactoryInstance.INSTANCE;
    }

    private static class MessageFactoryInstance {

	private static final MessageFactory INSTANCE = new MessageFactory();
    }

    private MessageFactory() {
    }

    private final Map<Integer, Class<? extends BaseHandler>> map = new HashMap<Integer, Class<? extends BaseHandler>>() {
	{
	    put(Constant.LOGIN, LoginAnswerHandler.class);
	}
    };

    /**
     * 生产具体消息处理逻辑
     * 
     * @param messageId
     * @return
     */
    public BaseHandler buildMessage(int messageId, String json) {
	synchronized (this.locker) {
	    if (!map.containsKey(messageId))
		return null;

	    Class<? extends BaseHandler> clazz = map.get(messageId);
	    try {
		BaseHandler obj = clazz.newInstance();
		obj.setJson(json);
		return obj;
	    } catch (Exception e) {
		return null;
	    }
	}
    }

    public boolean canHandel(int messageId) {
	return this.map.containsKey(messageId);
    }
}
