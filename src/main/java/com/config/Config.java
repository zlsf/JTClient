package com.config;

public class Config {
    /** IP地址 */
    private String ip = "127.0.0.1";
    /** 端口号 */
    private int port = 8899;
    /** 6位 */
    private String deviceId = "000000";

    public Config() {
    }

    private Config(String ip, int port, String deviceId) {
	this.ip = ip;
	this.port = port;
	this.deviceId = deviceId;
    }

    public String getIp() {
	return ip;
    }

    public void setIp(String ip) {
	this.ip = ip;
    }

    public int getPort() {
	return port;
    }

    public void setPort(int port) {
	this.port = port;
    }

    public String getDeviceId() {
	return deviceId;
    }

    public void setDeviceId(String deviceId) {
	this.deviceId = deviceId;
    }

}
