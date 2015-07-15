package cn.edu.bit.linc.uniformsql.network.server;

import cn.edu.bit.linc.uniformsql.network.packets.HandShakePacket;
import cn.edu.bit.linc.uniformsql.network.packets.Packet;
import cn.edu.bit.linc.uniformsql.network.packets.PacketExceptions;
import cn.edu.bit.linc.uniformsql.network.packets.PacketHeader;
import cn.edu.bit.linc.uniformsql.network.utils.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 统一 SQL 系统 Socket Handler，用于接收和发送请求
 */
public class UniformSQLSocketHandler implements SocketHandler {
    private final int id;
    private final Socket clientSocket;

    /**
     * 构造函数，初始化 Socket Handler
     *
     * @param id           Socket 连接编号
     * @param clientSocket 客户端 Socket
     */
    public UniformSQLSocketHandler(int id, Socket clientSocket) {
        this.id = id;
        this.clientSocket = clientSocket;
    }


    /**
     * handle Socket 连接
     *
     * @throws IOException 获取 InputStream 或者 OutputStream 失败
     */
    @Override
    public void handleSocket() throws IOException {
        Log.info(clientSocket.getInetAddress() + " has been connected to server with ID " + id);

        InputStream in;
        OutputStream out;

        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();

        // TODO: 发送握手数据包
        Packet handShakePacket = null;
        try {
            handShakePacket = buildHandShakePacket();
            byte[] data = new byte[handShakePacket.getSize()];
            handShakePacket.getData(data);
            out.write(data);
        } catch (PacketExceptions.NecessaryFieldNotSetException ex) {
            // TODO: 正确处理异常
            Log.info("必要字段 var_len 尚未确定", ex);
            return;
        }


        // TODO: 接收客户端验证包
        // TODO: 验证
        // TODO: 发送认证结果包
        // TODO: 接收结果包
        while (true) {
            // TODO: 接收到断开连接请求
            break;
        }

        // 关闭连接
        closeQuietly(in);
        closeQuietly(out);
    }

    /**
     * 关闭流
     *
     * @param closeable 需要关闭的流
     */
    private void closeQuietly(Closeable closeable) {
        if (closeable != null) try {
            closeable.close();
        } catch (IOException e) {
            // TODO: do something
        } finally { /* we tried! */ }
    }

    /**
     * 构建握手数据包
     *
     * @return 构建得到的握手数据包
     * @throws cn.edu.bit.linc.uniformsql.network.packets.PacketExceptions.NecessaryFieldNotSetException 必要字段没有设置
     */
    private Packet buildHandShakePacket() throws PacketExceptions.NecessaryFieldNotSetException {
        /* 构建握手包 */
        // TODO: 从系统全局变量中读取协议版本和服务器版本
        byte protocolVersion = 1;               // 协议版本
        String serverVersion = "Version 0.1";   // 服务器版本
        int threadID = (int) Thread.currentThread().getId();            // 线程 ID
        // TODO: 配置未知区域
        byte[] unknownFiledOne = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9}; // 不明区域一
        // TODO: 定义服务器权能
        int serverCapabilities = 4;             // 服务器权能
        byte characterSet = 5;                  // 服务器使用的字符集
        // TODO: 定义服务器状态
        int serverStatus = 6;                   // 服务器状态
        byte[] unknownFiledTwo = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};     // 不明区域二

        HandShakePacket handShakePacket = new HandShakePacket(serverVersion.getBytes().length + 1 + 45);
        handShakePacket.setProtocolVersion(protocolVersion);
        handShakePacket.setServerVersion(serverVersion);
        handShakePacket.setThreadID(threadID);
        handShakePacket.setUnknownFieldOne(unknownFiledOne);
        handShakePacket.setServerCapabilities(serverCapabilities);
        handShakePacket.setCharacterSet(characterSet);
        handShakePacket.setServerStatus(serverStatus);
        handShakePacket.setUnknownFieldTwo(unknownFiledTwo);
        byte[] body = new byte[handShakePacket.getSize()];
        handShakePacket.getData(body);

        /* 构建包头 */
        PacketHeader packetHeader = new PacketHeader(4);
        packetHeader.setPacketLength(handShakePacket.getSize());
        packetHeader.setPacketID((byte) 0);     // TODO: 包序列号

        /* 构建数据包 */
        Packet packet = new Packet(packetHeader.getSize() + body.length);
        packet.setPacketHeader(packetHeader);
        packet.setPacketBody(body);

        return packet;
    }

}
