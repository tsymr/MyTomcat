package NIO.servlet;

import NIO.utils.NIORequest;
import NIO.utils.NIOResponse;

public interface NIOServlet {

    void service(NIORequest request, NIOResponse response);

    void init();

    void destroy();
}
