package NIO.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class NIOResponse {

    private final SelectionKey selectionKey;

    public NIOResponse(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public void write(String out) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK \r\n")
                .append("Content-Type:text/html;charset=utf-8 \r\n")
                .append("\r\n")
                .append(out);
        ByteBuffer buffer = ByteBuffer.wrap(sb.toString().getBytes());
        SocketChannel channel = (SocketChannel) selectionKey.channel();

        channel.write(buffer);
        selectionKey.cancel();
        channel.close();
    }
}
