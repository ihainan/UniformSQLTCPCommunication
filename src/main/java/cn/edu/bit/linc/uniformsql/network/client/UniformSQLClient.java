package cn.edu.bit.linc.uniformsql.network.client;

import java.io.IOException;
import java.net.Socket;

/**
 * 客户端
 */

public class UniformSQLClient {
    private final int port;             // 服务器端口
    private final String serverHost;    // 服务器地址
    private final ClientSocketHandlerFactory socketHandlerFactory;

    /**
     * 构造函数
     *
     * @param serverHost           远程服务器地址
     * @param port                 远程服务器端口
     * @param socketHandlerFactory Socket Handler 工厂
     */
    private UniformSQLClient(String serverHost, int port, ClientSocketHandlerFactory socketHandlerFactory) {
        this.port = port;
        this.serverHost = serverHost;
        this.socketHandlerFactory = socketHandlerFactory;
    }

    /**
     * 连接远程服务器
     *
     * @throws IOException
     */
    public void connect() throws IOException {
        Socket clientSocket = new Socket(serverHost, port);
        final ClientSocketHandler handler = socketHandlerFactory.newSocketHandler(clientSocket);
        handler.handleSocket();
    }

    /**
     * Uniform SQL Server 构建器
     */
    public static class Builder {
        private Integer port;         // 服务器端口
        private String serverHost;    // 服务器地址
        private ClientSocketHandlerFactory socketHandlerFactory;

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
        public Builder withSocketHandlerFactory(ClientSocketHandlerFactory socketHandlerFactory) {
            this.socketHandlerFactory = socketHandlerFactory;
            return this;
        }

        /**
         * 配置远程服务器地址
         *
         * @param serverHost 远程服务器地址
         * @return Builder
         */
        public Builder toServer(String serverHost) {
            this.serverHost = serverHost;
            return this;
        }

        /**
         * 构建 UniformSQLClient 对象
         *
         * @return UniformSQLClient 对象
         */
        public UniformSQLClient build() {
            if (port == null || serverHost == null) {
                throw new IllegalStateException("port and server host do not have defaults");
            }
            return new UniformSQLClient(serverHost, port, socketHandlerFactory);
        }
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) throws IOException {
        int port = 9527;
        String host = "127.0.0.1";
        UniformSQLClientSocketHandlerFactory uniformSQLClientSocketHandlerFactory = new UniformSQLClientSocketHandlerFactory();
        UniformSQLClient client = new UniformSQLClient.Builder()
                .onPort(port).toServer(host)
                .withSocketHandlerFactory(uniformSQLClientSocketHandlerFactory)
                .build();

        client.connect();
    }
}