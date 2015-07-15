package cn.edu.bit.linc.uniformsql.network.server;

import java.net.Socket;

/**
 * Socket Handler 工厂
 */
public interface SocketHandlerFactory {
    public SocketHandler newSocketHandler(Socket socket);
}
