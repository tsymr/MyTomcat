package NIO;

import NIO.servlet.NIOServletImpl;
import NIO.utils.NIORequest;
import NIO.utils.NIOResponse;

import java.io.IOException;

public class HelloServlet extends NIOServletImpl {
    @Override
    public void doGet(NIORequest request, NIOResponse response) {
        try {
            response.write("Hello NIO GET");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doPost(NIORequest request, NIOResponse response) {
        try {
            System.out.println("body = " + request.getBody().replaceAll(" ", ""));
            response.write("Hello NIO POST");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
