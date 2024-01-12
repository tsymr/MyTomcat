import handler.HttpRequestHandler;

import java.io.IOException;
import java.net.ServerSocket;

public class MyTomcatV2 {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);

        while (!serverSocket.isClosed()){
            new Thread(new HttpRequestHandler(serverSocket.accept())).start();
        }
    }
}
