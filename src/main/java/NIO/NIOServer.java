package NIO;

import NIO.servlet.NIOServlet;
import NIO.utils.NIORequest;
import NIO.utils.NIOResponse;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class NIOServer {

    private Selector selector;

    private int port;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private ConcurrentHashMap<String, Object> servletMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, String> urlMap = new ConcurrentHashMap<>();


    public NIOServer(int port) {
        this.port = port;
    }

    /**
     * nio tomcat启动
     *
     * @throws IOException
     */
    public void start() throws IOException {
        initServlet();
        // 启动Selector
        selector = Selector.open();
        // 开启Channel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 配置通道非阻塞
        serverSocketChannel.configureBlocking(false);
        // 绑定端口
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.accept();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("NIO Tomcat is Running on port:" + port);

        // 使用队列存储请求和响应
        ConcurrentLinkedQueue<NIORequest> requests = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<NIOResponse> responses = new ConcurrentLinkedQueue<>();
        // 轮询查询
        while (true) {
            // 选择一些I/O已经造作好的管道， 每个管道对应一个key
            // 该步操作时一个阻塞选择的操作，当前至少有一个管道被选择。
            int n = selector.select();
            if (n > 0) {
                // 获取监听器的所有事件并进行业务处理
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();

                    // 连接事件
                    if (selectionKey.isAcceptable()) {
                        // 开启读取监听
                        doRead(selectionKey);
                    } else if (selectionKey.isReadable()) {
                        requests.add(new NIORequest(selectionKey));
                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                    } else if (selectionKey.isWritable()) {
                        responses.add(new NIOResponse(selectionKey));
                        selectionKey.interestOps(SelectionKey.OP_READ);
                    }
                    if (!requests.isEmpty() && !responses.isEmpty()) {
                        doPath(requests.poll(), responses.poll());
                    }
                    iterator.remove();
                }
            }
        }
    }

    private void doPath(final NIORequest request, final NIOResponse response) {
        if (request == null || response == null) {
            return;
        }
        final String url = request.getUrl();
        if (null != url) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String servletName = urlMap.get(url);
                    if (null == servletName) {
                        try {
                            response.write("404 NOT FOUND");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                    NIOServlet servlet = (NIOServlet) servletMap.get(servletName);
                    if (null == servlet) {
                        try {
                            response.write("404 NOT FOUND");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                    servlet.service(request, response);
                }
            });
        } else {
            try {
                response.write("404 NOT FOUND");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void doRead(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel;
        try {
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    private void initServlet() {
        //获取编译后的文件锁在classPath
        String path = Objects.requireNonNull(NIOTomcat.class.getResource("/")).getPath();
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
                    urlMap.put(ele.element("url-pattern").getText(), ele.element("servlet-name").getText());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
