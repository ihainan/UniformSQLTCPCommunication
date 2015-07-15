package cn.edu.bit.linc.uniformsql.network.server;

import java.io.IOException;

/**
 * Server Socket handler
 */
public interface ServerSocketHandler {
    /**
     * 控制 Socket 连接
     * @throws IOException 获取 InputStream 或者 OutputStream 失败
     */
    public void handleSocket() throws IOException;
}
