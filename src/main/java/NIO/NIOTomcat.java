package NIO;

import java.io.IOException;

public class NIOTomcat {

    public static void main(String[] args) {
        NIOServer nioServer = new NIOServer(8080);
        try {
            nioServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
