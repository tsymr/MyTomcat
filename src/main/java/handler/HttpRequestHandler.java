package handler;

import httpUtils.HttpRequest;
import httpUtils.HttpResponse;
import servlet.MyHttpServlet;
import servlet.MyTestServlet;

import java.io.*;
import java.net.Socket;

public class HttpRequestHandler implements Runnable {

    private Socket socket;

    public HttpRequestHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            HttpRequest httpRequest = new HttpRequest(inputStream);
            System.out.println(httpRequest);
            OutputStream outputStream = socket.getOutputStream();
            HttpResponse httpResponse = new HttpResponse(outputStream);
            MyTestServlet myTestServlet = new MyTestServlet();
            myTestServlet.service(httpRequest, httpResponse);
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
