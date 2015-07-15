package cn.edu.bit.linc.uniformsql.network.client;

import java.io.IOException;

/**
 * Client socket handler
 */
public interface ClientSocketHandler {
    /**
     * 控制 Socket 连接
     * @throws java.io.IOException 获取 InputStream 或者 OutputStream 失败
     */
    public void handleSocket() throws IOException;
}
