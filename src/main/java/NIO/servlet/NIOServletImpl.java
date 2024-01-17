package NIO.servlet;

import NIO.utils.NIORequest;
import NIO.utils.NIOResponse;

public abstract class NIOServletImpl implements NIOServlet {
    @Override
    public void service(NIORequest request, NIOResponse response) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            this.doGet(request, response);
        }
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            this.doPost(request, response);
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {

    }

    public abstract void doGet(NIORequest request, NIOResponse response);

    public abstract void doPost(NIORequest request, NIOResponse response);

}
