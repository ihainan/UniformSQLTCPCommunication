package cn.edu.bit.linc.uniformsql.network.server;

import java.io.IOException;

/**
 * Socket handler
 */
public interface SocketHandler {
    /**
     * 控制 Socket 连接
     * @throws IOException 获取 InputStream 或者 OutputStream 失败
     */
    public void handleSocket() throws IOException;
}
