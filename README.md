## 实现简单的Tomcat
1. 自定义servlet接口，设计server()方法以及init()和destroy()方法
2. 自定义HttpServlet抽象类，继承servlet接口实现server()等方法，定义抽象方法doGet()和doPost()方法用于处理请求
3. 定义Servlet，继承HttpServlet实现doGet和doPost方法
4. 自定义HttpRequest 和 HttpResponse 根据socket中的 InputStream 和 OutputStream 后去请求信息以及返回数据 
5. 使用dom4j实现init()方法读取web.xml内容初始化 uri-servletName关联map 和 servletName-servlet实例关联map 
6. main方法新建一个ServiceSocket监听本地端口，遇到请求链接则启动新的线程进入HttpRequestHandler
7. 在HttpRequestHandler中根据InputStream和OutputStream完成HttpRequest 和 HttpResponse的初始化
8. 根据HttpRequest中的uri获取到相应的servlet实例，调用servlet实例server()方法完成对请求的处理