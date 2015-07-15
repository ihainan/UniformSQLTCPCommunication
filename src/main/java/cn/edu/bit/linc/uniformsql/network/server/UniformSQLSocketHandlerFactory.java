package cn.edu.bit.linc.uniformsql.network.server;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 统一 SQL 系统 Socket Handler 工厂
 */
public class UniformSQLSocketHandlerFactory implements SocketHandlerFactory {
    private final AtomicInteger nextId = new AtomicInteger();

    @Override
    public SocketHandler newSocketHandler(Socket socket) {
        int id = nextId.getAndIncrement();
        return new UniformSQLSocketHandler(id, socket);
    }
}
