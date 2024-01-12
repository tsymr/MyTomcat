package httpUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private InputStream inputStream;

    private String method;

    private String uri;
    private Map<String,Object> params;

    public HttpRequest(InputStream inputStream) {
        this.inputStream = inputStream;
        this.params = new HashMap<>();
        init();
    }

    private void init(){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
       try {
           String info =  bufferedReader.readLine();
           String[] infos = info.split(" ");
           this.setMethod(infos[0]);

           String uriAndParams = infos[1];

           int index = uriAndParams.indexOf("?");
           if(index > -1){
               setUri(uriAndParams.substring(0, index));

               String params = uriAndParams.substring(index + 1);
               if(!params.isEmpty()){
                   // a=1&b=1  a=&b=1  a&b=1  a=1&b a=1&b=
                   String[] parmaList = params.split("&");
                   for (String param : parmaList) {
                       String[] paramPair =  param.split("=");
                       if(paramPair.length == 2){
                           this.params.put(paramPair[0], paramPair[1]);
                       }
                   }
               }

           }else {
               setUri(uriAndParams);
           }


       }catch (IOException e){
           throw new RuntimeException(e);
       }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "inputStream=" + inputStream +
                ", method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", params=" + params +
                '}';
    }
}
