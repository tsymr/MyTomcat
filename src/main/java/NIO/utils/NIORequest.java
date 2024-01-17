package NIO.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class NIORequest {

    private String method;
    private String url;

    private String body;

    public NIORequest(SelectionKey selectionKey) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.read(buffer);
        buffer.flip();
        String request = new String(buffer.array()).trim();
        if (!request.isEmpty()) {
            String requestHeaders = request.split("\n")[0];
            url = requestHeaders.split("\\s")[1].split("\\?")[0];
            method = requestHeaders.split("\\s")[0];
            if ("POST".equalsIgnoreCase(method)) {
                String temp = request.replaceAll("[\t\n\r]", "");
                int bodyStartIndex = temp.indexOf("{");
                body = temp.substring(bodyStartIndex);
            }
        }

    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "NIORequest{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
