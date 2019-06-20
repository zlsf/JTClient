package com.socket;

import java.net.Socket;

import com.config.Config;

public class Sender extends AbstractSender {
    private Sender() {
    }

    public static Sender getInstance(Config config) {
	Sender.config = config;
	return SenderInstance.INSTANCE;
    }

    private static class SenderInstance {
	private static final Sender INSTANCE = new Sender();
    }

    private static Config config;
    private Socket socket;
}
