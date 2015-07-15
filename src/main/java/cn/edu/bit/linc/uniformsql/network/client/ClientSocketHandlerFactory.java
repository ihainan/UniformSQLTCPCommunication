package cn.edu.bit.linc.uniformsql.network.client;

import java.net.Socket;

/**
 * Server Socket Handler 工厂
 */
public interface ClientSocketHandlerFactory {
    public ClientSocketHandler newSocketHandler(Socket socket);
}
