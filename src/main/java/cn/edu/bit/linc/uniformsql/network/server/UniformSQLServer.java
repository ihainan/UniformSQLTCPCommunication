package cn.edu.bit.linc.uniformsql.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.edu.bit.linc.uniformsql.network.utils.*;

/**
 * 服务器端
 */
public class UniformSQLServer {
    private final int port;
    private final int timeout;
    private final SocketHandlerFactory socketHandlerFactory;
    private final ExecutorService handlerService = Executors.newCachedThreadPool();

    /**
     * 构造函数
     *
     * @param port                 服务器端口
     * @param timeout              Socket 连接超时
     * @param socketHandlerFactory Socket Handler 工厂
     */
    private UniformSQLServer(int port, int timeout, SocketHandlerFactory socketHandlerFactory) {
        this.port = port;
        this.timeout = timeout;
        this.socketHandlerFactory = socketHandlerFactory;
    }

    /**
     * 启动服务端
     *
     * @throws IOException 创建 Server Socket 失败
     */
    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(timeout); // TODO: 对 Socket 超时机制的完善

        Log.info("Network: Waiting for connection on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            final SocketHandler handler = socketHandlerFactory.newSocketHandler(clientSocket);

            handlerService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        handler.handleSocket();
                    } catch (IOException ex) {
                        // TODO: 异常处理
                        Log.error("** Error", ex);
                    }
                }
            });
        }
    }

    /**
     * Uniform SQL Server 构建器
     */
    public static class Builder {
        private Integer port;
        private Integer timeout;
        private SocketHandlerFactory socketHandlerFactory;

        /**
         * 配置超时时长
         *
         * @param timeout 超时时长，单位为 ms
         * @return Builder
         */
        public Builder withTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        /**
         * 配置服务器开放端口
         *
         * @param port 服务器开放端口
         * @return Builder
         */
        public Builder onPort(int port) {
            this.port = port;
            return this;
        }

        /**
         * 配置 Socket Handler 工厂
         *
         * @param socketHandlerFactory Socket Handler 工厂
         * @return Builder
         */
        public Builder withSocketHandlerFactory(SocketHandlerFactory socketHandlerFactory) {
            this.socketHandlerFactory = socketHandlerFactory;
            return this;
        }

        /**
         * 获取 Uniform SQL Server 对象
         *
         * @return Uniform SQL Server 对象
         */
        public UniformSQLServer build() {
            if (port == null || timeout == null) {
                throw new IllegalStateException("port and timeout do not have defaults");
            }
            return new UniformSQLServer(port, timeout, socketHandlerFactory);
        }
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     * @throws java.io.IOException 创建 Server Socket 失败
     */
    public static void main(String[] args) throws IOException {
        int port = 9527;
        int timeout = 100000;
        UniformSQLSocketHandlerFactory uniformSQLSocketHandlerFactory = new UniformSQLSocketHandlerFactory();

        UniformSQLServer server = new UniformSQLServer
                .Builder()
                .onPort(port)
                .withTimeout(timeout)
                .withSocketHandlerFactory(uniformSQLSocketHandlerFactory)
                .build();

        server.start();
    }
}
