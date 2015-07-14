package cn.edu.bit.linc.uniformsql.network.packets;

import java.util.Arrays;

/**
 * 握手数据包.
 */
public class HandShakePacket extends BasePacket {
    /**
     * 协议版本字段在 byte 数组中的偏移量
     */
    public static int OFFSET_PROTOCOL_VERSION = 0;

    /**
     * 服务器版本在 byte 数组中的偏移量
     */
    public static int OFFSET_SERVER_VERSION = 1;

    /**
     * 服务器版本字段长度
     */
    private int varLen = -1;

    /**
     * 线程编号字段偏移量 - 服务器版本字段长度
     */
    public static int OFFSET_THREAD_ID_MINUS_SV_LENGTH = 1;

    /**
     * 未知字段一偏移量 - 服务器版本字段长度
     */
    public static int OFFSET_UNKNOWN_ONE_MINUS_SV_LENGTH = 5;

    /**
     * 服务器权能标志字段偏移量 - 服务器版本字段长度
     */
    public static int OFFSET_SERVER_CAPABILITIES_MINUS_SV_LENGTH = 14;

    /**
     * 服务器端字符编码字段偏移量 - 服务器版本字段长度
     */
    public static int OFFSET_CHARACTER_SET_MINUS_SV_LENGTH = 16;

    /**
     * 服务器状态字段偏移量 - 服务器版本字段长度
     */
    public static int OFFSET_SERVER_STATUS_MINUS_SV_LENGTH = 17;

    /**
     * 保留字段偏移量 - 服务器版本字段长度
     */
    public static int OFFSET_RESERVED_FIELD_MINUS_SV_LENGTH = 19;

    /**
     * 未知字段二偏移量 - 服务器版本字段长度
     */
    public static int OFFSET_UNKNOWN_TWO_MINUS_SV_LENGTH = 5;

    /**
     * 创建指定大小的包头数据包
     *
     * @param size 指定大小（字节为单位）
     */
    public HandShakePacket(int size) {
        super(size);
    }

    /**
     * 设置协议版本字段
     *
     * @param protocolVersion 协议版本
     */
    public void setProtocolVersion(byte protocolVersion) {
        _data_[OFFSET_PROTOCOL_VERSION] = protocolVersion;
    }

    /**
     * 获取协议版本字段
     *
     * @return 协议版本
     */
    public byte getProtocolVersion() {
        return _data_[OFFSET_PROTOCOL_VERSION];
    }

    /**
     * 设置服务器版本字段
     *
     * @param serverVersion 服务器版本
     */
    public void setServerVersion(String serverVersion) {
        byte[] strBytes = serverVersion.getBytes();
        System.arraycopy(strBytes, 0, _data_, OFFSET_SERVER_VERSION, strBytes.length);
        _data_[strBytes.length + OFFSET_SERVER_VERSION] = 0;
        this.varLen = strBytes.length + 1;
    }

    /**
     * 获取服务器版本字段
     *
     * @return 服务器版本字段
     */
    public String getServerVersion() {
        byte[] strBytes = new byte[this.varLen - 1];
        System.arraycopy(_data_, OFFSET_SERVER_VERSION, strBytes, 0, strBytes.length);
        return new String(strBytes);
    }

    /**
     * 设置服务器端线程 ID 字段
     *
     * @param threadID 服务器端线程 ID
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public void setThreadID(int threadID) throws PacketExceptions.NecessaryFieldNotSetException {
        if (this.varLen == -1) {
            throw new PacketExceptions.NecessaryFieldNotSetException();
        }

        setBytes(_data_, this.varLen + OFFSET_THREAD_ID_MINUS_SV_LENGTH, 4, threadID);
    }

    /**
     * 获取服务器端线程 ID 字段
     *
     * @return 服务器端线程 ID 字段值
     */
    public int getThreadID() {
        if (this.varLen == -1) {
            // TODO: 获取 varLen
        }
        return getInt(_data_, this.varLen + OFFSET_THREAD_ID_MINUS_SV_LENGTH, 4);
    }

    /**
     * 设置未知字段一
     *
     * @param unknownFiled 未知字段一
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public void setUnknownFieldOne(byte[] unknownFiled) throws PacketExceptions.NecessaryFieldNotSetException {
        if (this.varLen == -1) {
            throw new PacketExceptions.NecessaryFieldNotSetException();
        }

        System.arraycopy(unknownFiled, 0, _data_, this.varLen + OFFSET_UNKNOWN_ONE_MINUS_SV_LENGTH, 9);
    }


    /**
     * 获取未知字段一
     *
     * @return 未知字段一
     */
    public byte[] getUnknownFieldOne() {
        if (this.varLen == -1) {
            // TODO: 获取 varLen
        }

        byte[] unknownFiled = new byte[9];
        System.arraycopy(_data_, this.varLen + OFFSET_UNKNOWN_ONE_MINUS_SV_LENGTH, unknownFiled, 0, unknownFiled.length);
        return unknownFiled;
    }

    public static void main(String[] agrs) throws PacketExceptions.NecessaryFieldNotSetException {
        byte protocolVersion = 2;
        String serverVersion = "v0.1";
        int threadID = 2;
        byte[] unknownFiledOne = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        int serverCapbilities = 2;
        byte characterSet = 2;
        int serverStatus = 2;
        byte[] reversedField = new byte[13];
        byte[] unknownFiledTwo = new byte[13];

        HandShakePacket handShakePacket = new HandShakePacket(serverVersion.getBytes().length + 1 + 45);
        // handShakePacket.setThreadID(threadID);   // Exception
        handShakePacket.setProtocolVersion(protocolVersion);
        handShakePacket.setServerVersion(serverVersion);
        handShakePacket.setThreadID(threadID);
        handShakePacket.setUnknownFieldOne(unknownFiledOne);

        System.out.println(handShakePacket);

        System.out.println("Protocol Version: " + handShakePacket.getProtocolVersion());
        System.out.println("Server Version: " + handShakePacket.getServerVersion());
        System.out.println(handShakePacket.getThreadID());
        System.out.println(Arrays.toString(handShakePacket.getUnknownFieldOne()));
    }
}
