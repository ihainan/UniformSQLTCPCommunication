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
    public static int OFFSET_UNKNOWN_TWO_MINUS_SV_LENGTH = 32;

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

        return getIntFromBytes(_data_, this.varLen + OFFSET_THREAD_ID_MINUS_SV_LENGTH, 4);
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

    /**
     * 设置服务器权能字段
     *
     * @param capabilities 服务器权能
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public void setServerCapabilities(int capabilities) throws PacketExceptions.NecessaryFieldNotSetException {
        if (this.varLen == -1) {
            throw new PacketExceptions.NecessaryFieldNotSetException();
        }

        setBytes(_data_, this.varLen + OFFSET_SERVER_CAPABILITIES_MINUS_SV_LENGTH, 2, capabilities);
    }

    /**
     * 获取服务器权能字段
     *
     * @return 服务器全能字段
     */
    public int getServerCapabilities() {
        if (this.varLen == -1) {
            // TODO: 获取 varLen
        }

        return getIntFromBytes(_data_, this.varLen + OFFSET_SERVER_CAPABILITIES_MINUS_SV_LENGTH, 2);
    }

    /**
     * 设置服务器字符编码字段
     *
     * @param characterSet 服务器字符编码
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public void setCharacterSet(int characterSet) throws PacketExceptions.NecessaryFieldNotSetException {
        if (this.varLen == -1) {
            throw new PacketExceptions.NecessaryFieldNotSetException();
        }

        setBytes(_data_, this.varLen + OFFSET_CHARACTER_SET_MINUS_SV_LENGTH, 1, characterSet);
    }

    /**
     * 获取服务器字符编码
     *
     * @return 服务器字符编码
     */
    public byte getCharacterSet() {
        if (this.varLen == -1) {
            // TODO: 获取 varLen
        }

        return (byte) getIntFromBytes(_data_, this.varLen + OFFSET_CHARACTER_SET_MINUS_SV_LENGTH, 1);
    }

    /**
     * 设置服务器状态字段
     *
     * @param serverStatus 服务器状态
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public void setServerStatus(int serverStatus) throws PacketExceptions.NecessaryFieldNotSetException {
        if (this.varLen == -1) {
            throw new PacketExceptions.NecessaryFieldNotSetException();
        }

        setBytes(_data_, this.varLen + OFFSET_SERVER_STATUS_MINUS_SV_LENGTH, 2, serverStatus);
    }

    /**
     * 获取服务器状态字段
     *
     * @return 服务器装填
     */
    public int getServerStatus() {
        if (this.varLen == -1) {
            // TODO: 获取 varLen
        }

        return getIntFromBytes(_data_, this.varLen + OFFSET_SERVER_STATUS_MINUS_SV_LENGTH, 2);
    }

    /**
     * 设置未知字段二
     *
     * @param unknownFiled 未知字段二
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public void setUnknownFieldTwo(byte[] unknownFiled) throws PacketExceptions.NecessaryFieldNotSetException {
        if (this.varLen == -1) {
            throw new PacketExceptions.NecessaryFieldNotSetException();
        }

        System.arraycopy(unknownFiled, 0, _data_, this.varLen + OFFSET_UNKNOWN_TWO_MINUS_SV_LENGTH, 13);
    }


    /**
     * 获取未知字段二
     *
     * @return 未知字段二
     */
    public byte[] getUnknownFieldTwo() {
        if (this.varLen == -1) {
            // TODO: 获取 varLen
        }

        byte[] unknownFiled = new byte[13];
        System.arraycopy(_data_, this.varLen + OFFSET_UNKNOWN_TWO_MINUS_SV_LENGTH, unknownFiled, 0, unknownFiled.length);
        return unknownFiled;
    }

    /**
     * 测试函数
     *
     * @param agrs 程序参数
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public static void main(String[] agrs) throws PacketExceptions.NecessaryFieldNotSetException {
        byte protocolVersion = 2;
        String serverVersion = "Version 0.1";
        int threadID = 3;
        byte[] unknownFiledOne = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        int serverCapabilities = 4;
        byte characterSet = 5;
        int serverStatus = 6;
        byte[] unknownFiledTwo = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

        HandShakePacket handShakePacket = new HandShakePacket(serverVersion.getBytes().length + 1 + 45);
        // handShakePacket.setThreadID(threadID);   // NecessaryFieldNotSetException
        handShakePacket.setProtocolVersion(protocolVersion);
        handShakePacket.setServerVersion(serverVersion);
        handShakePacket.setThreadID(threadID);
        handShakePacket.setUnknownFieldOne(unknownFiledOne);
        handShakePacket.setServerCapabilities(serverCapabilities);
        handShakePacket.setCharacterSet(characterSet);
        handShakePacket.setServerStatus(serverStatus);
        handShakePacket.setUnknownFieldTwo(unknownFiledTwo);

        System.out.println(handShakePacket);

        System.out.println("Protocol Version    : " + handShakePacket.getProtocolVersion());
        System.out.println("Server Version      : " + handShakePacket.getServerVersion());
        System.out.println("Thread ID           : " + handShakePacket.getThreadID());
        System.out.println("Unknown Field One   : " + Arrays.toString(handShakePacket.getUnknownFieldOne()));
        System.out.println("Server Capabilities : " + handShakePacket.getServerCapabilities());
        System.out.println("Character Set       : " + handShakePacket.getCharacterSet());
        System.out.println("Server Status       : " + handShakePacket.getServerStatus());
        System.out.println("Unknown Field Two   : " + Arrays.toString(handShakePacket.getUnknownFieldTwo()));

        System.out.println();   // 检测偏移量是否设置正确

        System.out.println("Protocol Version    : " + handShakePacket.getProtocolVersion());
        System.out.println("Server Version      : " + handShakePacket.getServerVersion());
        System.out.println("Thread ID           : " + handShakePacket.getThreadID());
        System.out.println("Unknown Field One   : " + Arrays.toString(handShakePacket.getUnknownFieldOne()));
        System.out.println("Server Capabilities : " + handShakePacket.getServerCapabilities());
        System.out.println("Character Set       : " + handShakePacket.getCharacterSet());
        System.out.println("Server Status       : " + handShakePacket.getServerStatus());
        System.out.println("Unknown Field Two   : " + Arrays.toString(handShakePacket.getUnknownFieldTwo()));
    }
    /* Output:
    [2, 86, 101, 114, 115, 105, 111, 110, 32, 48, 46, 49, 0, 0, 0, 0, 3, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 4, 5, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]
    Protocol Version    : 2
    Server Version      : Version 0.1
    Thread ID           : 3
    Unknown Field One   : [1, 2, 3, 4, 5, 6, 7, 8, 9]
    Server Capabilities : 2571
    Character Set       : 12
    Server Status       : 3334
    Unknown Field Two   : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]

    Protocol Version    : 2
    Server Version      : Version 0.1
    Thread ID           : 3
    Unknown Field One   : [1, 2, 3, 4, 5, 6, 7, 8, 9]
    Server Capabilities : 4
    Character Set       : 5
    Server Status       : 6
    Unknown Field Two   : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13]
     */
}

