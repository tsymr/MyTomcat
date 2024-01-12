package servlet;

import httpUtils.HttpRequest;
import httpUtils.HttpResponse;

import java.io.IOException;

public class MyTestServlet extends MyHttpServlet{
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        System.out.println("uri = " + request.getUri());
        System.out.println("method = " + request.getMethod());
        System.out.println("params = " + request.getParams());
        String message = "<h1>测试自定义Servlet "+ Thread.currentThread().getName() +"</h1>";
        try {
            response.getOutputStream().write((response.getResponseHeader() + response.getResponseContentType() + message).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        doGet(request, response);
    }
}
