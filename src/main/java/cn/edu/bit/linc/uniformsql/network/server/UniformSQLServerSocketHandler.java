package cn.edu.bit.linc.uniformsql.network.server;

import cn.edu.bit.linc.uniformsql.network.packets.*;
import cn.edu.bit.linc.uniformsql.network.packets.type.IntegerType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeBinaryType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeStringType;
import cn.edu.bit.linc.uniformsql.network.packets.type.StringType;
import cn.edu.bit.linc.uniformsql.network.utils.Log;
import org.apache.commons.lang.RandomStringUtils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * 统一 SQL 系统 Server Socket Handler，用于接收和发送请求
 */
public class UniformSQLServerSocketHandler implements ServerSocketHandler {
    private final int id;
    private final Socket clientSocket;

    /**
     * 构造函数，初始化 Socket Handler
     *
     * @param id           Socket 连接编号
     * @param clientSocket 客户端 Socket
     */
    public UniformSQLServerSocketHandler(int id, Socket clientSocket) {
        this.id = id;
        this.clientSocket = clientSocket;
    }


    /**
     * handle Socket 连接
     *
     * @throws IOException 获取 InputStream 或者 OutputStream 失败
     */

    public void handleSocket() throws IOException {
        Log.info(clientSocket.getInetAddress() + " has been connected to server with ID " + id);

        InputStream in = clientSocket.getInputStream();
        OutputStream out = clientSocket.getOutputStream();

        // TODO: 发送握手数据包
        Packet handShakePacket = null;
        try {
            handShakePacket = buildHandShakePacket();
            byte[] data = new byte[handShakePacket.getSize()];
            handShakePacket.getData(data);
            out.write(data);
            Log.info("Send hand shake packet to client " + clientSocket.getInetAddress());
            Log.info("handShakePacket : " + handShakePacket + "\r\n");
        } catch (PacketExceptions.NecessaryFieldNotSetException ex) {
            // TODO: 正确处理异常
            Log.info("必要字段 var_len 尚未确定", ex);
            return;
        }


        // TODO: 接收客户端验证包
        Packet packer = readPacket(in);
        byte[] credentialsPacketBytes = packer.getPacketBody();
        CredentialsPacket credentialsPacket = new CredentialsPacket(credentialsPacketBytes.length);
        credentialsPacket.setData(credentialsPacketBytes);
        Log.info("Received credentials packet from server " + clientSocket.getInetAddress());
        Log.info("credentialsPacket : " + credentialsPacket);
        System.out.println("Character Set       : " + IntegerType.getIntegerValue(credentialsPacket.getCharacterSet()));
        System.out.println("Max Packet Length   : " + IntegerType.getIntegerValue(credentialsPacket.getMaxPacketLength()));
        System.out.println("Server Capabilities : " + IntegerType.getIntegerValue(credentialsPacket.getClientCapabilities()));
        CredentialsPacket.CredentialInformation credentialInformation1 = credentialsPacket.getCredentialInformation();
        System.out.println("User Name           : " + StringType.getString(credentialInformation1.userName));
        System.out.println("Token               : " + LengthCodeStringType.getString(credentialInformation1.token));
        System.out.println("Database Name       : " + StringType.getString(credentialInformation1.dbName));
        System.out.println();

        // TODO: 验证
        // TODO: 发送认证结果包
        Packet successPacket = null;
        try {
            successPacket = buildSuccessPacket();
            byte[] data = new byte[successPacket.getSize()];
            successPacket.getData(data);
            out.write(data);
            Log.info("Send success packet to client " + clientSocket.getInetAddress());
            Log.info("successPacket : " + successPacket + "\r\n");
        } catch (Exception ex) {
            // TODO: 正确处理异常
            return;
        }

        // 接收命令包
        packer = readPacket(in);
        byte[] commandBytes = packer.getPacketBody();
        CommandPacket commandPacket = new CommandPacket(commandBytes.length);
        commandPacket.setData(commandBytes);
        Log.info("Received command packet from server " + clientSocket.getInetAddress());
        Log.info("commandPacket : " + commandPacket);
        System.out.println("Command Code      : " + IntegerType.getIntegerValue(commandPacket.getCommandCode()));
        System.out.println("Command           : " + LengthCodeStringType.getString(commandPacket.getCommand()));
        System.out.println();

        // 发送结果包（测试）
        try {
            successPacket = buildSuccessPacket();
            byte[] data = new byte[successPacket.getSize()];
            successPacket.getData(data);
            out.write(data);
            Log.info("Send success packet to client " + clientSocket.getInetAddress());
            Log.info("successPacket : " + successPacket + "\r\n");
        } catch (Exception ex) {
            // TODO: 正确处理异常
            return;
        }

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

    /**
     * 构建握手数据包
     *
     * @return 构建得到的握手数据包
     * @throws cn.edu.bit.linc.uniformsql.network.packets.PacketExceptions.NecessaryFieldNotSetException 必要字段没有设置
     */
    private Packet buildHandShakePacket() throws PacketExceptions.NecessaryFieldNotSetException {
        /* 构建握手包 */

        // TODO: 从系统全局变量中读取协议版本和服务器版本

        IntegerType protocolVersion = IntegerType.getIntegerType(2, HandShakePacket.LENGTH_PROTOCOL_VERSION);
        StringType serverVersion = StringType.getStringType("Version 0.1");
        IntegerType threadID = IntegerType.getIntegerType(3, HandShakePacket.LENGTH_THREAD_ID);
        IntegerType serverCapabilities = IntegerType.getIntegerType(4, HandShakePacket.LENGTH_SERVER_CAPABILITIES);
        IntegerType characterSet = IntegerType.getIntegerType(5, HandShakePacket.LENGTH_CHARACTER_SET);
        IntegerType serverStatus = IntegerType.getIntegerType(6, HandShakePacket.LENGTH_SERVER_STATUS);

        String randomStr = RandomStringUtils.randomAlphanumeric(HandShakePacket.LENGTH_SCRAMBLE_ONE + HandShakePacket.LENGTH_SCRAMBLE_TWO - 2);    // 随机字符串
        StringType randomStrPartOneST = StringType.getStringType(randomStr.substring(0, 8));
        byte[] scramblePartOne = new byte[HandShakePacket.LENGTH_SCRAMBLE_ONE];
        randomStrPartOneST.getData(scramblePartOne);
        StringType randomStrPartTwoST = StringType.getStringType(randomStr.substring(8, 20));
        byte[] scramblePartTwo = new byte[HandShakePacket.LENGTH_SCRAMBLE_TWO];
        randomStrPartTwoST.getData(scramblePartTwo);

        HandShakePacket handShakePacket = new HandShakePacket(serverVersion.getSize() + 45);
        handShakePacket.setProtocolVersion(protocolVersion);
        handShakePacket.setServerVersion(serverVersion);
        handShakePacket.setThreadID(threadID);
        handShakePacket.setScramblePartOne(scramblePartOne);
        handShakePacket.setServerCapabilities(serverCapabilities);
        handShakePacket.setCharacterSet(characterSet);
        handShakePacket.setServerStatus(serverStatus);
        handShakePacket.setScramblePartTwo(scramblePartTwo);
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
        //return null;
    }

    /**
     * 构建响应成功数据包
     *
     * @return 构建得到的响应成功数据包
     */
    private Packet buildSuccessPacket() {

        IntegerType packetIdentifier = IntegerType.getIntegerType(0x00, SuccessPacket.LENGTH_PACKET_IDENTIFIER);
        LengthCodeBinaryType changedRows = LengthCodeBinaryType.getLengthCodeBinaryType(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        LengthCodeBinaryType indexID = LengthCodeBinaryType.getLengthCodeBinaryType(new byte[] {4, 3, 2, 1});
        IntegerType serverStatus = IntegerType.getIntegerType(200, SuccessPacket.LENGTH_SERVER_STATUS);
        IntegerType warningNumber = IntegerType.getIntegerType(2, SuccessPacket.LENGTH_WARNING_NUMBER);
        LengthCodeStringType serverMessage = LengthCodeStringType.getLengthCodeString("OK");

        SuccessPacket successPacket = new SuccessPacket(packetIdentifier.getSize() + changedRows.getSize() + indexID.getSize() + serverStatus.getSize() + warningNumber.getSize() + serverMessage.getSize());
        successPacket.setPacketIdentifier(packetIdentifier);
        successPacket.setChangedRows(changedRows);
        successPacket.setIndexID(indexID);
        successPacket.setServerStatus(serverStatus);
        successPacket.setWarningNumber(warningNumber);
        successPacket.setServerMessage(serverMessage);
        byte[] body = new byte[successPacket.getSize()];
        successPacket.getData(body);

        /* 构建包头 */
        PacketHeader packetHeader = new PacketHeader(4);
        packetHeader.setPacketLength(successPacket.getSize());
        packetHeader.setPacketID((byte) 0);     // TODO: 包序列号

        /* 构建数据包 */
        Packet packet = new Packet(packetHeader.getSize() + body.length);
        packet.setPacketHeader(packetHeader);
        packet.setPacketBody(body);

        return packet;

    }

}
