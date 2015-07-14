package cn.edu.bit.linc.uniformsql.network.packets;

import java.util.Arrays;

/**
 * 包头（Packet Header）<br>
 * <ul>
 * <li>包长度（Packet Length），3 字节</li>
 * <li>包编号（Packet ID），1 字节</li>
 * </ul>
 */
public class PacketHeader extends BasePacket {
    /**
     * 包长度域在 byte 数组中的偏移量
     */
    public static final int OFFSET_PACKET_LENGTH = 0;

    /**
     * 包编号在 byte 数组中的偏移量
     */
    public static final int OFFSET_PACKET_ID = 3;

    /**
     * 创建指定大小的包头数据包
     *
     * @param size 指定大小（字节为单位）
     */
    public PacketHeader(int size) {
        super(size);
    }

    /**
     * 设置包长度值
     *
     * @param packetLength 包长度值
     */
    public final void setPacketLength(int packetLength) {
        _data_[OFFSET_PACKET_LENGTH] = (byte) ((packetLength >> 16) & 0xff);
        _data_[OFFSET_PACKET_LENGTH + 1] = (byte) ((packetLength >> 8) & 0xff);
        _data_[OFFSET_PACKET_LENGTH + 2] = (byte) (packetLength & 0xff);
    }

    /**
     * 获取包长度值
     *
     * @return 包长度值
     */
    public final int getPacketLength() {
        return ((_data_[OFFSET_PACKET_LENGTH] & 0xff) << 16)
                + ((_data_[OFFSET_PACKET_LENGTH + 1] & 0xff) << 8)
                + (_data_[OFFSET_PACKET_LENGTH + 2] & 0xff);
    }

    /**
     * 设置包编号
     *
     * @param id 包编号
     */
    public final void setPacketID(byte id) {
        _data_[OFFSET_PACKET_ID] = (byte) (id & 0xff);
    }

    /**
     * 获取包编号
     *
     * @return 包编号
     */
    public final byte getPacketID() {
        return (byte) (_data_[OFFSET_PACKET_ID] & 0xff);
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {
        PacketHeader packetHeader = new PacketHeader(4);
        packetHeader.setPacketLength(20);
        packetHeader.setPacketID((byte) 1);

        System.out.println(packetHeader.getPacketLength());
        System.out.println(packetHeader.getPacketID());

        byte[] data = new byte[packetHeader.getSize()];
        packetHeader.getData(data);
        System.out.println(Arrays.toString(data));

        String str = "Hello World";
        byte[] strByte = str.getBytes();
        System.out.println(strByte.length);
    }
}
