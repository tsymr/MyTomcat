import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class MyTomcat {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);
        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String info = "";
            while (bufferedReader.readLine() != null) {
                info = bufferedReader.readLine();
                if (info.isEmpty()) {
                    break;
                }
                System.out.println("info = " + info);
            }
            OutputStream outputStream = socket.getOutputStream();

            String responseHeader = "HTTP/1.1 200 OK \r\n" + "Content-Type:text/html;charset=utf-8\r\n\r\n";
            String response = responseHeader + "<h1>myTomcat  测试 " + Thread.currentThread().getName() + "</h1>";
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            socket.close();
        }
    }
}
