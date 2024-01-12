package httpUtils;

import java.io.OutputStream;

public class HttpResponse {

    private OutputStream outputStream;

    private String responseHeader = "HTTP/1.1 200 Ok \r\n";
    private  String responseContentType = "Content-Type:text/html;charset=utf-8 \r\n\r\n";

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(String responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }
}
