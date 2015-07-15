package cn.edu.bit.linc.uniformsql.network.client;

import java.net.Socket;

/**
 * 统一 SQL 系统 Server Socket Handler 工厂
 */
public class UniformSQLClientSocketHandlerFactory implements ClientSocketHandlerFactory {
    @Override
    public ClientSocketHandler newSocketHandler(Socket socket) {
        return new UniformSQLClientSocketHandler(socket);
    }
}
