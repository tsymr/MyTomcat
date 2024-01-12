package servlet;

import httpUtils.HttpRequest;
import httpUtils.HttpResponse;

public interface MyServlet {

     void service(HttpRequest request, HttpResponse response);

     void  init();

     void destroy();
}
