package test;

import com.client.DataServer;
import com.client.SessionManager;
import com.config.Config;
import com.kj.datacenter.msg.TestMessage;

public class Test1 {
    public static Config c;

    public static void main(String[] args) {
	c = new Config();
	c.setIp("192.168.99.192");
	c.setPort(8899);
	DataServer server = new DataServer(c);

	try {
	    Thread.sleep(10000);
	    SessionManager.getInstance().SendMessage(new TestMessage());
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
