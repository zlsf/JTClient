package test;
import com.client.DataServer;
import com.config.Config;

public class Test1 {
    public static Config c;

    public static void main(String[] args) {
	c = new Config();
	c.setIp("192.168.99.192");
	c.setPort(8899);
	DataServer server = new DataServer(c);
    }
}
