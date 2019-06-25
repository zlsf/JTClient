package com.message;

public abstract class BaseHandler {

    public abstract void dealMessage();

    protected String json;

    public String getJson() {
	return json;
    }

    public void setJson(String json) {
	this.json = json;
    }

}
