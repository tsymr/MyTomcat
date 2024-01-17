package NIO;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class NIOTomcat {
    public static ConcurrentHashMap<String, Object> servletMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> uriMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        NIOServer nioServer = new NIOServer(8080);
        try {
            nioServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
