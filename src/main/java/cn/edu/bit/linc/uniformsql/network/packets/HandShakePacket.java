package cn.edu.bit.linc.uniformsql.network.packets;

import cn.edu.bit.linc.uniformsql.network.packets.type.IntegerType;
import cn.edu.bit.linc.uniformsql.network.packets.type.StringType;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Arrays;

/**
 * 握手数据报文
 */
public class HandShakePacket extends BasePacket {
    /**
     * 协议版本字段在 byte 数组中的偏移量
     */
    public final static int OFFSET_PROTOCOL_VERSION = 0;

    /**
     * 协议版本字段的长度
     */
    public final static int LENGTH_PROTOCOL_VERSION = 1;

    /**
     * 服务器版本在 byte 数组中的偏移量
     */
    public final static int OFFSET_SERVER_VERSION = 1;

    /**
     * 服务器版本字段长度
     */
    private int varLen = -1;

    /**
     * 线程编号字段偏移量 - 服务器版本字段长度
     */
    public final static int OFFSET_THREAD_ID_MINUS_SV_LENGTH = 1;

    /**
     * 协议版本字段的长度
     */
    public final static int LENGTH_THREAD_ID = 4;

    /**
     * 挑战数字段（前半） - 服务器版本字段长度
     */
    public final static int OFFSET_SCRAMBLE_ONE_MINUS_SV_LENGTH = 5;

    /**
     * 挑战数字段（前半）的长度
     */
    public final static int LENGTH_SCRAMBLE_ONE = 9;

    /**
     * 服务器权能标志字段偏移量 - 服务器版本字段长度
     */
    public final static int OFFSET_SERVER_CAPABILITIES_MINUS_SV_LENGTH = 14;

    /**
     * 服务器权能标志字段的长度
     */
    public final static int LENGTH_SERVER_CAPABILITIES = 2;

    /**
     * 服务器端字符编码字段偏移量 - 服务器版本字段长度
     */
    public final static int OFFSET_CHARACTER_SET_MINUS_SV_LENGTH = 16;

    /**
     * 服务器端字符编码字段的长度
     */
    public final static int LENGTH_CHARACTER_SET = 1;

    /**
     * 服务器状态字段偏移量 - 服务器版本字段长度
     */
    public final static int OFFSET_SERVER_STATUS_MINUS_SV_LENGTH = 17;

    /**
     * 服务器端字符编码字段的长度
     */
    public final static int LENGTH_SERVER_STATUS = 2;

    /**
     * 保留字段偏移量 - 服务器版本字段长度
     */
    public final static int OFFSET_RESERVED_FIELD_MINUS_SV_LENGTH = 19;

    /**
     * 挑战数（后半）字段偏移量 - 服务器版本字段长度
     */
    public final static int OFFSET_SCRAMBLE_TWO_MINUS_SV_LENGTH = 32;

    /**
     * 挑战数（后半）字段的长度
     */
    public final static int LENGTH_SCRAMBLE_TWO = 13;


    /**
     * 创建指定大小的包头数据报文
     *
     * @param size 指定大小（字节为单位）
     */
    public HandShakePacket(int size) {
        super(size);
    }

    /**
     * 设置 _data_ 数组，同时确定不定长字段的位置
     *
     * @param data 指定 byte 数组
     */
    @Override
    public void setData(byte[] data) {
        super.setData(data);

        calVarLen();
    }

    /**
     * 获取得到 varLen 的值
     */
    private void calVarLen() {
        // 获取 varLen
        varLen = 1;
        int currentPos = OFFSET_SERVER_VERSION;
        while (currentPos < _data_.length && _data_[currentPos++] != 0) {
            varLen++;
        }
    }

    /**
     * 设置协议版本字段
     *
     * @param protocolVersion 协议版本
     */
    public void setProtocolVersion(IntegerType protocolVersion) {
        byte[] bytes = new byte[protocolVersion.getSize()];
        protocolVersion.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_PROTOCOL_VERSION, bytes.length);
    }

    /**
     * 获取协议版本字段
     *
     * @return 协议版本
     */
    public IntegerType getProtocolVersion() {
        byte[] data = new byte[LENGTH_PROTOCOL_VERSION];
        System.arraycopy(_data_, OFFSET_PROTOCOL_VERSION, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }


    /**
     * 设置服务器版本字段
     *
     * @param serverVersion 服务器版本
     */
    public void setServerVersion(StringType serverVersion) {
        byte[] strBytes = new byte[serverVersion.getSize()];
        serverVersion.getData(strBytes);
        System.arraycopy(strBytes, 0, _data_, OFFSET_SERVER_VERSION, strBytes.length);
        this.varLen = strBytes.length;
    }

    /**
     * 获取服务器版本字段
     *
     * @return 服务器版本字段
     */
    public StringType getServerVersion() {
        byte[] data = new byte[this.varLen];
        System.arraycopy(_data_, OFFSET_SERVER_VERSION, data, 0, data.length);
        return StringType.getStringType(data);
    }

    /**
     * 设置服务器端线程 ID 字段
     *
     * @param threadID 服务器端线程 ID
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public void setThreadID(IntegerType threadID) throws PacketExceptions.NecessaryFieldNotSetException {
        if (this.varLen == -1) {
            throw new PacketExceptions.NecessaryFieldNotSetException();
        }

        byte[] bytes = new byte[threadID.getSize()];
        threadID.getData(bytes);
        System.arraycopy(bytes, 0, _data_, this.varLen + OFFSET_THREAD_ID_MINUS_SV_LENGTH, bytes.length);
    }

    /**
     * 获取服务器端线程 ID 字段
     *
     * @return 服务器端线程 ID 字段值
     */
    public IntegerType getThreadID() {
        if (this.varLen == -1) {
            calVarLen();
        }

        byte[] data = new byte[LENGTH_THREAD_ID];
        System.arraycopy(_data_, this.varLen + OFFSET_THREAD_ID_MINUS_SV_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置挑战数前半部分
     *
     * @param scrambleNumberPartOne 挑战数前半部分
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public void setScramblePartOne(byte[] scrambleNumberPartOne) throws PacketExceptions.NecessaryFieldNotSetException {
        if (this.varLen == -1) {
            throw new PacketExceptions.NecessaryFieldNotSetException();
        }

        System.arraycopy(scrambleNumberPartOne, 0, _data_, this.varLen + OFFSET_SCRAMBLE_ONE_MINUS_SV_LENGTH, LENGTH_SCRAMBLE_ONE);
    }

    /**
     * 获取挑战数前半部分
     *
     * @return 挑战数前半部分
     */
    public byte[] getScramblePartOne() {
        if (this.varLen == -1) {
            calVarLen();
        }

        byte[] scrambleNumberPartOne = new byte[LENGTH_SCRAMBLE_ONE];
        System.arraycopy(_data_, this.varLen + OFFSET_SCRAMBLE_ONE_MINUS_SV_LENGTH, scrambleNumberPartOne, 0, scrambleNumberPartOne.length);
        return scrambleNumberPartOne;
    }

    /**
     * 设置服务器权能字段
     *
     * @param capabilities 服务器权能
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public void setServerCapabilities(IntegerType capabilities) throws PacketExceptions.NecessaryFieldNotSetException {
        if (this.varLen == -1) {
            throw new PacketExceptions.NecessaryFieldNotSetException();
        }

        byte[] bytes = new byte[capabilities.getSize()];
        capabilities.getData(bytes);
        System.arraycopy(bytes, 0, _data_, this.varLen + OFFSET_SERVER_CAPABILITIES_MINUS_SV_LENGTH, bytes.length);
    }

    /**
     * 获取服务器权能字段
     *
     * @return 服务器全能字段
     */
    public IntegerType getServerCapabilities() {
        if (this.varLen == -1) {
            calVarLen();
        }

        byte[] data = new byte[LENGTH_SERVER_CAPABILITIES];
        System.arraycopy(_data_, this.varLen + OFFSET_SERVER_CAPABILITIES_MINUS_SV_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置服务器字符编码字段
     *
     * @param characterSet 服务器字符编码
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public void setCharacterSet(IntegerType characterSet) throws PacketExceptions.NecessaryFieldNotSetException {
        if (this.varLen == -1) {
            throw new PacketExceptions.NecessaryFieldNotSetException();
        }

        byte[] bytes = new byte[characterSet.getSize()];
        characterSet.getData(bytes);
        System.arraycopy(bytes, 0, _data_, this.varLen + OFFSET_CHARACTER_SET_MINUS_SV_LENGTH, bytes.length);
    }

    /**
     * 获取服务器字符编码
     *
     * @return 服务器字符编码
     */
    public IntegerType getCharacterSet() {
        if (this.varLen == -1) {
            calVarLen();
        }

        byte[] data = new byte[LENGTH_CHARACTER_SET];
        System.arraycopy(_data_, this.varLen + OFFSET_CHARACTER_SET_MINUS_SV_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置服务器状态字段
     *
     * @param serverStatus 服务器状态
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public void setServerStatus(IntegerType serverStatus) throws PacketExceptions.NecessaryFieldNotSetException {
        if (this.varLen == -1) {
            throw new PacketExceptions.NecessaryFieldNotSetException();
        }

        byte[] bytes = new byte[serverStatus.getSize()];
        serverStatus.getData(bytes);
        System.arraycopy(bytes, 0, _data_, this.varLen + OFFSET_SERVER_STATUS_MINUS_SV_LENGTH, bytes.length);
    }

    /**
     * 获取服务器状态字段
     *
     * @return 服务器装填
     */
    public IntegerType getServerStatus() {
        if (this.varLen == -1) {
            calVarLen();
        }

        byte[] data = new byte[LENGTH_SERVER_STATUS];
        System.arraycopy(_data_, this.varLen + OFFSET_SERVER_STATUS_MINUS_SV_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置半部分挑战数后半部分
     *
     * @param scrambleNumberPartTwo 挑战数后半部分
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public void setScramblePartTwo(byte[] scrambleNumberPartTwo) throws PacketExceptions.NecessaryFieldNotSetException {
        if (this.varLen == -1) {
            throw new PacketExceptions.NecessaryFieldNotSetException();
        }

        System.arraycopy(scrambleNumberPartTwo, 0, _data_, this.varLen + OFFSET_SCRAMBLE_TWO_MINUS_SV_LENGTH, LENGTH_SCRAMBLE_TWO);
    }


    /**
     * 获取挑战数后半部分
     *
     * @return 挑战数后半部分
     */
    public byte[] getScramblePartTwo() {
        if (this.varLen == -1) {
            calVarLen();
        }

        byte[] scrambleNumberPartTwo = new byte[LENGTH_SCRAMBLE_TWO];
        System.arraycopy(_data_, this.varLen + OFFSET_SCRAMBLE_TWO_MINUS_SV_LENGTH, scrambleNumberPartTwo, 0, scrambleNumberPartTwo.length);
        return scrambleNumberPartTwo;
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     * @throws PacketExceptions.NecessaryFieldNotSetException varLen 未设置
     */
    public static void main(String[] args) throws PacketExceptions.NecessaryFieldNotSetException {
        IntegerType protocolVersion = IntegerType.getIntegerType(2, LENGTH_PROTOCOL_VERSION);
        StringType serverVersion = StringType.getStringType("Version 0.1");
        IntegerType threadID = IntegerType.getIntegerType(3, LENGTH_THREAD_ID);
        IntegerType serverCapabilities = IntegerType.getIntegerType(4, LENGTH_SERVER_CAPABILITIES);
        IntegerType characterSet = IntegerType.getIntegerType(5, LENGTH_CHARACTER_SET);
        IntegerType serverStatus = IntegerType.getIntegerType(6, LENGTH_SERVER_STATUS);

        String randomStr = RandomStringUtils.randomAlphanumeric(LENGTH_SCRAMBLE_ONE + LENGTH_SCRAMBLE_TWO - 2);    // 随机字符串
        StringType randomStrPartOneST = StringType.getStringType(randomStr.substring(0, 8));
        byte[] scramblePartOne = new byte[LENGTH_SCRAMBLE_ONE];
        randomStrPartOneST.getData(scramblePartOne);
        StringType randomStrPartTwoST = StringType.getStringType(randomStr.substring(8, 20));
        byte[] scramblePartTwo = new byte[LENGTH_SCRAMBLE_TWO];
        randomStrPartTwoST.getData(scramblePartTwo);

        HandShakePacket handShakePacket = new HandShakePacket(serverVersion.getSize() + 45);
        // handShakePacket.setThreadID(threadID);   // NecessaryFieldNotSetException
        handShakePacket.setProtocolVersion(protocolVersion);
        handShakePacket.setServerVersion(serverVersion);
        handShakePacket.setThreadID(threadID);
        handShakePacket.setScramblePartOne(scramblePartOne);
        handShakePacket.setServerCapabilities(serverCapabilities);
        handShakePacket.setCharacterSet(characterSet);
        handShakePacket.setServerStatus(serverStatus);
        handShakePacket.setScramblePartTwo(scramblePartTwo);

        System.out.println(handShakePacket);

        System.out.println(randomStr);
        System.out.println("Protocol Version    : " + IntegerType.getIntegerValue(handShakePacket.getProtocolVersion()));
        System.out.println("Server Version      : " + StringType.getString(handShakePacket.getServerVersion()));
        System.out.println("Thread ID           : " + IntegerType.getIntegerValue(handShakePacket.getThreadID()));
        System.out.println("Scramble Part One   : " + Arrays.toString(handShakePacket.getScramblePartOne()));
        System.out.println("Server Capabilities : " + IntegerType.getIntegerValue(handShakePacket.getServerCapabilities()));
        System.out.println("Character Set       : " + IntegerType.getIntegerValue(handShakePacket.getCharacterSet()));
        System.out.println("Server Status       : " + IntegerType.getIntegerValue(handShakePacket.getServerStatus()));
        System.out.println("Scramble Part Two   : " + Arrays.toString(handShakePacket.getScramblePartTwo()));

        System.out.println();   // 检测偏移量是否设置正确

        System.out.println("Protocol Version    : " + IntegerType.getIntegerValue(handShakePacket.getProtocolVersion()));
        System.out.println("Server Version      : " + StringType.getString(handShakePacket.getServerVersion()));
        System.out.println("Thread ID           : " + IntegerType.getIntegerValue(handShakePacket.getThreadID()));
        System.out.println("Scramble Part One   : " + Arrays.toString(handShakePacket.getScramblePartOne()));
        System.out.println("Server Capabilities : " + IntegerType.getIntegerValue(handShakePacket.getServerCapabilities()));
        System.out.println("Character Set       : " + IntegerType.getIntegerValue(handShakePacket.getCharacterSet()));
        System.out.println("Server Status       : " + IntegerType.getIntegerValue(handShakePacket.getServerStatus()));
        System.out.println("Scramble Part Two   : " + Arrays.toString(handShakePacket.getScramblePartTwo()));
    }
    /* Output:
    [2, 86, 101, 114, 115, 105, 111, 110, 32, 48, 46, 49, 0, 0, 0, 0, 3, 104, 108, 72, 113, 78, 55, 48, 119, 0, 0, 4, 5, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 101, 74, 50, 82, 74, 53, 73, 108, 102, 53, 70, 98, 0]
    hlHqN70weJ2RJ5Ilf5Fb
    Protocol Version    : 2
    Server Version      : Version 0.1
    Thread ID           : 3
    Scramble Part One   : [104, 108, 72, 113, 78, 55, 48, 119, 0]
    Server Capabilities : 4
    Character Set       : 5
    Server Status       : 6
    Scramble Part Two   : [101, 74, 50, 82, 74, 53, 73, 108, 102, 53, 70, 98, 0]

    Protocol Version    : 2
    Server Version      : Version 0.1
    Thread ID           : 3
    Scramble Part One   : [104, 108, 72, 113, 78, 55, 48, 119, 0]
    Server Capabilities : 4
    Character Set       : 5
    Server Status       : 6
    Scramble Part Two   : [101, 74, 50, 82, 74, 53, 73, 108, 102, 53, 70, 98, 0]
     */
}

