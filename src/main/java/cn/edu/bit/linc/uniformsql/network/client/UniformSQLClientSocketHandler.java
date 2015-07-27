package cn.edu.bit.linc.uniformsql.network.client;

import cn.edu.bit.linc.uniformsql.network.packets.*;
import cn.edu.bit.linc.uniformsql.network.packets.type.IntegerType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeBinaryType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeStringType;
import cn.edu.bit.linc.uniformsql.network.packets.type.StringType;
import cn.edu.bit.linc.uniformsql.network.utils.CHAP;
import cn.edu.bit.linc.uniformsql.network.utils.Log;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

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
        Log.info("handShakePacket : " + handShakePacket);
        System.out.println("Protocol Version    : " + IntegerType.getIntegerValue(handShakePacket.getProtocolVersion()));
        System.out.println("Server Version      : " + StringType.getString(handShakePacket.getServerVersion()));
        System.out.println("Thread ID           : " + IntegerType.getIntegerValue(handShakePacket.getThreadID()));
        System.out.println("Scramble Part One   : " + Arrays.toString(handShakePacket.getScramblePartOne()));
        System.out.println("Server Capabilities : " + IntegerType.getIntegerValue(handShakePacket.getServerCapabilities()));
        System.out.println("Character Set       : " + IntegerType.getIntegerValue(handShakePacket.getCharacterSet()));
        System.out.println("Server Status       : " + IntegerType.getIntegerValue(handShakePacket.getServerStatus()));
        System.out.println("Scramble Part Two   : " + Arrays.toString(handShakePacket.getScramblePartTwo()));
        System.out.println();

        // TODO: 构造和发送验证包
        Packet credentialsPacket = null;
        try {
            credentialsPacket = buildCredentialsPacket();
            byte[] data = new byte[credentialsPacket.getSize()];
            credentialsPacket.getData(data);
            out.write(data);
            Log.info("Send credentials packet to server " + clientSocket.getInetAddress());
            Log.info("credentialsPacket : " + credentialsPacket + "\r\n");
        } catch (Exception e) {
            // TODO: 正确处理异常
            //Log.info("必要字段 var_len 尚未确定", ex);
            return;
        }

        // TODO: 接收验证结果包
        packer = readPacket(in);
        byte[] SuccessPacketBytes = packer.getPacketBody();
        SuccessPacket successPacket = new SuccessPacket(SuccessPacketBytes.length);
        successPacket.setData(SuccessPacketBytes);
        Log.info("Received success packet from server " + clientSocket.getInetAddress());
        Log.info("successPacket : " + successPacket);
        System.out.println("Packet Identifier : " + IntegerType.getIntegerValue(successPacket.getPacketIdentifier()));
        System.out.print("Changed Rows      : ");
        byte[] bytes = LengthCodeBinaryType.getBytes(successPacket.getChangedRows());
        for(byte b : bytes) {
            System.out.print(b + " ");
        }
        System.out.println();
        System.out.print("Index ID          : ");
        bytes = LengthCodeBinaryType.getBytes(successPacket.getIndexID());
        for(byte b : bytes) {
            System.out.print(b + " ");
        }
        System.out.println();
        System.out.println("Server Status     : " + IntegerType.getIntegerValue(successPacket.getServerStatus()));
        System.out.println("Warning Number    : " + IntegerType.getIntegerValue(successPacket.getWarningNumber()));
        System.out.println("Server Message    : " + LengthCodeStringType.getString(successPacket.getServerMessage()));
        System.out.println();

        // 发送命令
        sendCommand(1, "SELECT * FROM table");

        // 接收结果
        packer = readPacket(in);
        byte[] ResultBytes = packer.getPacketBody();
        SuccessPacket resultPacket = new SuccessPacket(ResultBytes.length);
        resultPacket.setData(ResultBytes);
        Log.info("Received result packet from server " + clientSocket.getInetAddress());
        Log.info("resultPacket : " + resultPacket);
        System.out.println("Packet Identifier : " + IntegerType.getIntegerValue(resultPacket.getPacketIdentifier()));
        System.out.print("Changed Rows      : ");
        bytes = LengthCodeBinaryType.getBytes(resultPacket.getChangedRows());
        for(byte b : bytes) {
            System.out.print(b + " ");
        }
        System.out.println();
        System.out.print("Index ID          : ");
        bytes = LengthCodeBinaryType.getBytes(resultPacket.getIndexID());
        for(byte b : bytes) {
            System.out.print(b + " ");
        }
        System.out.println();
        System.out.println("Server Status     : " + IntegerType.getIntegerValue(resultPacket.getServerStatus()));
        System.out.println("Warning Number    : " + IntegerType.getIntegerValue(resultPacket.getWarningNumber()));
        System.out.println("Server Message    : " + LengthCodeStringType.getString(resultPacket.getServerMessage()));
        System.out.println();


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
        Packet commandPacket = null;
        try {
            IntegerType commandCode = IntegerType.getIntegerType(commandType, CommandPacket.LENGTH_COMMAND_CODE);
            LengthCodeStringType _command = LengthCodeStringType.getLengthCodeString(command);

            CommandPacket commandPacketBody = new CommandPacket(commandCode.getSize() + _command.getSize());
            commandPacketBody.setCommandCode(commandCode);
            commandPacketBody.setCommand(_command);
            byte[] body = new byte[commandPacketBody.getSize()];
            commandPacketBody.getData(body);

            /* 构建包头 */
            PacketHeader packetHeader = new PacketHeader(4);
            packetHeader.setPacketLength(commandPacketBody.getSize());
            packetHeader.setPacketID((byte) 0);     // TODO: 包序列号

            /* 构建数据包 */
            commandPacket = new Packet(packetHeader.getSize() + body.length);
            commandPacket.setPacketHeader(packetHeader);
            commandPacket.setPacketBody(body);

            byte[] data = new byte[commandPacket.getSize()];
            commandPacket.getData(data);
            out.write(data);
            Log.info("Send command packet to server " + clientSocket.getInetAddress());
            Log.info("commandPacket : " + commandPacket + "\r\n");
        } catch (Exception e) {
            // TODO: 正确处理异常
            //Log.info("必要字段 var_len 尚未确定", ex);
            return;
        }

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
            Log.error("Close stream failed", e);
        } finally { /* we tried! */ }
    }

    /**
     * 构造验证报文
     *
     * @return 验证报文
     */
    private Packet buildCredentialsPacket() {
        IntegerType capabilities = IntegerType.getIntegerType(5, CredentialsPacket.LENGTH_CLIENT_CAPABILITIES);
        IntegerType maxPacketLength = IntegerType.getIntegerType(10, CredentialsPacket.LENGTH_MAX_PACKET_LENGTH);
        IntegerType characterSet = IntegerType.getIntegerType(1, CredentialsPacket.LENGTH_CHARACTER_SET);
        StringType userName = StringType.getStringType("ihainan");
        String password = "12345";
        String scramble = "12345678901234567890";
        String tokenStr = CHAP.calcToken(password, scramble);
        LengthCodeStringType token = LengthCodeStringType.getLengthCodeString(tokenStr);
        StringType dbName = StringType.getStringType("db_test");
        CredentialsPacket.CredentialInformation credentialInformation = new CredentialsPacket.CredentialInformation(userName, token, dbName);

        CredentialsPacket credentialsPacket = new CredentialsPacket(32 + userName.getSize() + token.getSize() + dbName.getSize());
        credentialsPacket.setClientCapabilities(capabilities);
        credentialsPacket.setMaxPacketLength(maxPacketLength);
        credentialsPacket.setCharacterSet(characterSet);
        credentialsPacket.setCredentialInformation(credentialInformation);
        byte[] body = new byte[credentialsPacket.getSize()];
        credentialsPacket.getData(body);

        /* 构建包头 */
        PacketHeader packetHeader = new PacketHeader(4);
        packetHeader.setPacketLength(credentialsPacket.getSize());
        packetHeader.setPacketID((byte) 0);     // TODO: 包序列号

        /* 构建数据包 */
        Packet packet = new Packet(packetHeader.getSize() + body.length);
        packet.setPacketHeader(packetHeader);
        packet.setPacketBody(body);

        return packet;
    }
}
