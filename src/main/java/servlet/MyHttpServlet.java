package servlet;

import httpUtils.HttpRequest;
import httpUtils.HttpResponse;

public abstract class MyHttpServlet  implements MyServlet{


    @Override
    public void init() {

    }
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if("GET".equalsIgnoreCase(request.getMethod())){
            this.doGet(request, response);
        }
        if("POST".equalsIgnoreCase(request.getMethod())){
            this.doPost(request, response);
        }
    }

    public abstract void doGet(HttpRequest request, HttpResponse response);

    public abstract void doPost(HttpRequest request, HttpResponse response);


    @Override
    public void destroy() {

    }
}
