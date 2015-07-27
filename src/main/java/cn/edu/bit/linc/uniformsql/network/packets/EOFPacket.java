package cn.edu.bit.linc.uniformsql.network.packets;

import cn.edu.bit.linc.uniformsql.network.packets.type.IntegerType;
import cn.edu.bit.linc.uniformsql.network.packets.type.StringType;

/**
 * EOF报文
 */
public class EOFPacket extends BasePacket{

    /**
     * 报文类型标志的偏移量
     */
    public final static int OFFSET_PACKET_IDENTIFIER = 0;

    /**
     * 报文类型标志的长度
     */
    public final static int LENGTH_PACKET_IDENTIFIER = 1;

    /**
     * 警告数量的偏移量
     */
    public final static int OFFSET_WARNING_NUMBER = 1;

    /**
     * 警告数量的长度
     */
    public final static int LENGTH_WARNING_NUMBER = 2;

    /**
     * 状态标志位掩码的偏移量
     */
    public final static int OFFSET_SERVER_STATUS_BIT_MASK = 3;

    /**
     * 状态标志位掩码的长度
     */
    public final static int LENGTH_SERVER_STATUS_BIT_MASK = 2;

    /**
     * 创建指定大小的EOF报文
     *
     * @param size 指定大小（字节为单位）
     */
    public EOFPacket(int size) {
        super(size);
    }

    /**
     * 设置 _data_ 数组
     *
     * @param data 指定 byte 数组
     */
    @Override
    public void setData(byte[] data) {
        super.setData(data);
    }

    /**
     * 设置报文类型标志字段
     *
     * @param packetIdentifier 报文类型标志
     */
    public void setPacketIdentifier(IntegerType packetIdentifier) {
        byte[] bytes = new byte[packetIdentifier.getSize()];
        packetIdentifier.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_PACKET_IDENTIFIER, bytes.length);
    }

    /**
     * 获取报文类型标志字段
     *
     * @return 报文类型标志
     */
    public IntegerType getPacketIdentifier() {
        byte[] data = new byte[LENGTH_PACKET_IDENTIFIER];
        System.arraycopy(_data_, OFFSET_PACKET_IDENTIFIER, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置警告数量字段
     *
     * @param warningNumber 警告数量
     */
    public void setWarningNumber(IntegerType warningNumber) {
        byte[] bytes = new byte[warningNumber.getSize()];
        warningNumber.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_WARNING_NUMBER, bytes.length);
    }

    /**
     * 获取警告数量字段
     *
     * @return 警告数量
     */
    public IntegerType getWarningNumber() {
        byte[] data = new byte[LENGTH_WARNING_NUMBER];
        System.arraycopy(_data_, OFFSET_WARNING_NUMBER, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置状态标志位掩码字段
     *
     * @param serverStatusBitMask 状态标志位掩码
     */
    public void setServerStatusBitMask(IntegerType serverStatusBitMask) {
        byte[] bytes = new byte[serverStatusBitMask.getSize()];
        serverStatusBitMask.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_SERVER_STATUS_BIT_MASK, bytes.length);
    }

    /**
     * 获取状态标志位掩码字段
     *
     * @return 状态标志位掩码
     */
    public IntegerType getServerStatusBitMask() {
        byte[] data = new byte[LENGTH_SERVER_STATUS_BIT_MASK];
        System.arraycopy(_data_, OFFSET_SERVER_STATUS_BIT_MASK, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {

        IntegerType packetIdentifier = IntegerType.getIntegerType(0xFE, LENGTH_PACKET_IDENTIFIER);
        IntegerType warningNumber = IntegerType.getIntegerType(2, LENGTH_WARNING_NUMBER);
        IntegerType serverStatusBitMask = IntegerType.getIntegerType(0xFFFF, LENGTH_SERVER_STATUS_BIT_MASK);


        EOFPacket eofPacket = new EOFPacket(packetIdentifier.getSize() + warningNumber.getSize() + serverStatusBitMask.getSize());
        eofPacket.setPacketIdentifier(packetIdentifier);
        eofPacket.setWarningNumber(warningNumber);
        eofPacket.setServerStatusBitMask(serverStatusBitMask);

        System.out.println(eofPacket);
        System.out.println("Packet Identifier      : " + IntegerType.getIntegerValue(eofPacket.getPacketIdentifier()));
        System.out.println("Warning Number         : " + IntegerType.getIntegerValue(eofPacket.getWarningNumber()));
        System.out.println("Server Status Bit Mask : " + IntegerType.getIntegerValue(eofPacket.getServerStatusBitMask()));

        /* 通过byte[]构建 */
        EOFPacket eofPacketCopy = new EOFPacket(eofPacket.getSize());
        byte[] data = new byte[eofPacket.getSize()];
        eofPacket.getData(data);
        eofPacketCopy.setData(data);

        System.out.println();
        System.out.println(eofPacketCopy);
        System.out.println("Packet Identifier      : " + IntegerType.getIntegerValue(eofPacketCopy.getPacketIdentifier()));
        System.out.println("Warning Number         : " + IntegerType.getIntegerValue(eofPacketCopy.getWarningNumber()));
        System.out.println("Server Status Bit Mask : " + IntegerType.getIntegerValue(eofPacketCopy.getServerStatusBitMask()));

    }

    /* Output:
    [-2, 0, 2, -1, -1]
    Packet Identifier      : 254
    Warning Number         : 2
    Server Status Bit Mask : 65535

    Packet Identifier      : 254
    Warning Number         : 2
    Server Status Bit Mask : 65535
     */

}
