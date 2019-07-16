package test;

import com.client.DataServer;
import com.client.SessionManager;
import com.config.Config;
import com.kj.datacenter.msg.TestMessage;
import com.kj.datacenter.msg.ZLSFTest;

public class Test1 {
    public static Config c;

    public static void main(String[] args) {
	c = new Config();
	c.setIp("127.0.0.1");
	c.setPort(8899);
	DataServer server = new DataServer(c);

	try {
	    Thread.sleep(10000);
	    
	    ZLSFTest zlsf=new ZLSFTest();
	    zlsf.setName("name1111");
	    zlsf.setSex("age1111");
	    SessionManager.getInstance().SendMessage(zlsf);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
