package cn.edu.bit.linc.uniformsql.network.client;

import cn.edu.bit.linc.uniformsql.network.packets.HandShakePacket;
import cn.edu.bit.linc.uniformsql.network.packets.Packet;
import cn.edu.bit.linc.uniformsql.network.packets.PacketHeader;
import cn.edu.bit.linc.uniformsql.network.utils.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 统一 SQL 系统 Client Socket Handler，用于接收和发送请求
 */
public class UniformSQLClientSocketHandler implements ClientSocketHandler {
    private final Socket clientSocket;
    private InputStream in;
    private OutputStream out;

    /**
     * 构造函数
     *
     * @param clientSocket 客户端 Socket 连接
     */
    public UniformSQLClientSocketHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * handle Socket 连接
     *
     * @throws IOException 读取 InputStream 或者 OutputStream 失败
     */
    @Override
    public void handleSocket() throws IOException {
        Log.info("Connected to server " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();

        // TODO: 接收握手包
        Packet packer = readPacket(in);
        byte[] handShakePacketBytes = packer.getPacketBody();
        HandShakePacket handShakePacket = new HandShakePacket(handShakePacketBytes.length);
        handShakePacket.setData(handShakePacketBytes);
        Log.info("Received hand shake packet from server " + clientSocket.getInetAddress());

        // TODO: 构造和发送验证包
        // TODO: 接收验证结果包

        // 关闭连接（测试）
        closeQuietly(in);
        closeQuietly(out);
    }

    /**
     * 向远程服务器发送命令
     *
     * @param commandType 命令类型
     * @param command     命令字符串
     */
    public void sendCommand(int commandType, String command) {

    }

    /**
     * 从流中读取一个数据报文
     *
     * @param in 输入流
     * @return 读取得到的数据报文
     */
    public static Packet readPacket(InputStream in) throws IOException {
        /* 读取消息头 */
        byte[] packetHeaderBytes = new byte[4];
        in.read(packetHeaderBytes);
        PacketHeader packetHeader = new PacketHeader(4);
        packetHeader.setData(packetHeaderBytes);

        /* 读取消息体 */
        byte[] packetBodyBytes = new byte[packetHeader.getPacketLength()];
        in.read(packetBodyBytes);
        Packet packet = new Packet(4 + packetBodyBytes.length);
        packet.setPacketHeader(packetHeader);
        packet.setPacketBody(packetBodyBytes);

        return packet;
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
}
