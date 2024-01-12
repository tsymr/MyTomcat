import httpUtils.HttpRequest;
import httpUtils.HttpResponse;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import servlet.MyHttpServlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MyTomcatV3 {

    public static ConcurrentHashMap<String, Object> servletMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> uriMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        // 根据web.xml初始化servlet
        init();

        // 开启一个 ServerSocket 监听 8080端口
        ServerSocket serverSocket = new ServerSocket(8080);
        while (!serverSocket.isClosed()) {
            // 使用accept()方法获取到socket管道
            Socket socket = serverSocket.accept();
            // 从socket中获取到字节码输入流
            InputStream inputStream = socket.getInputStream();
            // 从socket中获取到字节码输出流
            OutputStream outputStream = socket.getOutputStream();
            // 根据输入输出流进行封装
            HttpRequest request = new HttpRequest(inputStream);
            HttpResponse response = new HttpResponse(outputStream);
            // 获取到请求的uri
            String uri = request.getUri();
            // 根据uri获取到servletName
            String servletName = uriMap.get(uri);
            if (null != servletName) {
                // 根据 servletName 获取到 servlet 实例
                MyHttpServlet servlet = (MyHttpServlet) servletMap.get(servletName);
                if (servlet != null) {
                    // 调用servlet的service方法处理请求
                    servlet.service(request, response);
                } else {
                    //如果没有找到servlet实例则返回404 NOT FOUND
                    response.getOutputStream().write((response.getResponseHeader() + response.getResponseContentType() + "<h1>404 NOT FOUND</h1>").getBytes());
                }
            } else {
                // 如果根据 uri 没有找到servletName 则返回404 NOT FOUND
                response.getOutputStream().write((response.getResponseHeader() + response.getResponseContentType() + "<h1>404 NOT FOUND</h1>").getBytes());
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
            socket.close();
        }
    }

    private static void init() {
        //获取编译后的文件锁在classPath
        String path = MyTomcatV3.class.getResource("/").getPath();
        String xmlPath = path + "web.xml";
        // 新建xml读取器
        SAXReader saxReader = new SAXReader();
        try {
            // 获取到xml的document
            Document document = saxReader.read(new File(xmlPath));
            // 获取到 xml 根节点内容
            Element element = document.getRootElement();
            // 获取到根节点下的所有节点
            List<Element> elements = element.elements();
            for (Element ele : elements) {
                if ("servlet".equalsIgnoreCase(ele.getName())) {
                    // 根据servlet中的 class文件路径 实例化 servlet 并将 servletName 和 servlet实例放入到 Map 中
                    servletMap.put(ele.element("servlet-name").getText(), Class.forName(ele.element("servlet-class").getText()).getDeclaredConstructor().newInstance());
                }
                if ("servlet-mapping".equalsIgnoreCase(ele.getName())) {
                    // 将 uri 与 servletName放入到 Map 中
                    uriMap.put(ele.element("url-pattern").getText(), ele.element("servlet-name").getText());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
