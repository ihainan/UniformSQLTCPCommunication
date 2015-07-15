package cn.edu.bit.linc.uniformsql.network.server;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 统一 SQL 系统 Server Socket Handler 工厂
 */
public class UniformSQLServerSocketHandlerFactory implements ServerSocketHandlerFactory {
    private final AtomicInteger nextId = new AtomicInteger();

    @Override
    public ServerSocketHandler newSocketHandler(Socket socket) {
        int id = nextId.getAndIncrement();
        return new UniformSQLServerSocketHandler(id, socket);
    }
}
