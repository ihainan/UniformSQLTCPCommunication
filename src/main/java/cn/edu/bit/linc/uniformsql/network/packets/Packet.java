package cn.edu.bit.linc.uniformsql.network.packets;

import java.util.Arrays;

/**
 * 基础报文 <br>
 * 报文包含两部分：<br>
 * <ul>
 * <li>消息头</li>
 * <li>消息体</li>
 * </ul>
 */
public class Packet extends BasePacket {
    /**
     * 消息头在 byte 数组中的偏移量
     */
    public static int OFFSET_PACKET_HEADER = 0;

    /**
     * 消息体在 byte 数组中的偏移量
     */
    public static int OFFSET_PACKET_BODY = 4;

    /**
     * 创建指定大小的包头数据包
     *
     * @param size 指定大小（字节为单位）
     */
    public Packet(int size) {
        super(size);
    }

    /**
     * 设置消息头
     *
     * @param header 消息头
     * @see cn.edu.bit.linc.uniformsql.network.packets.PacketHeader
     */
    public void setPacketHeader(PacketHeader header) {
        byte[] headerData = new byte[header.getSize()];
        header.getData(headerData);
        System.arraycopy(headerData, 0, _data_, OFFSET_PACKET_HEADER, headerData.length);
    }

    /**
     * 获取消息头
     *
     * @return 消息头
     * @see cn.edu.bit.linc.uniformsql.network.packets.PacketHeader
     */
    public PacketHeader getPacketHeader() {
        byte[] headerData = new byte[4];
        PacketHeader packetHeader = new PacketHeader(4);
        System.arraycopy(_data_, OFFSET_PACKET_HEADER, headerData, 0, headerData.length);
        packetHeader.setData(headerData);
        return packetHeader;
    }

    /**
     * 设置消息体
     *
     * @param body 消息体
     */
    public void setPacketBody(byte[] body) {
        // TODO: 长度检查
        System.arraycopy(body, 0, _data_, OFFSET_PACKET_BODY, body.length);
    }

    /**
     * 获取消息体
     *
     * @return 消息体
     */
    public byte[] getPacketBody() {
        byte[] body = new byte[getPacketHeader().getPacketLength()];
        System.arraycopy(_data_, OFFSET_PACKET_BODY, body, 0, body.length);
        return body;
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) throws PacketExceptions.NecessaryFieldNotSetException {
        // Header
        PacketHeader packetHeader = new PacketHeader(4);
        packetHeader.setPacketLength(5);
        packetHeader.setPacketID((byte) 1);
        byte protocolVersion = 2;
        String serverVersion = "Version 0.1";
        int threadID = 3;
        byte[] unknownFiledOne = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        int serverCapabilities = 4;
        byte characterSet = 5;
        int serverStatus = 6;
        byte[] unknownFiledTwo = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

        // Body
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

        // 构造包
        Packet packet = new Packet(packetHeader.getSize() + body.length);
        packet.setPacketHeader(packetHeader);
        packet.setPacketBody(body);

        // 输出
        System.out.println(packet);
        packetHeader = packet.getPacketHeader();
        System.out.println(packetHeader.getPacketLength());
        System.out.println(packetHeader.getPacketID());
        System.out.println(Arrays.toString(packet.getPacketBody()));
    }
    /* Output
    [0, 0, 5, 1, 2, 86, 101, 114, 115, 105, 111, 110, 32, 48, 46, 49, 0, 0, 0, 0, 3, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 4, 5, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]
    5
    1
    [2, 86, 101, 114, 115]
     */
}
