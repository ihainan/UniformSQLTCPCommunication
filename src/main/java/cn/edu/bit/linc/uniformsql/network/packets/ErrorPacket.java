package cn.edu.bit.linc.uniformsql.network.packets;

import cn.edu.bit.linc.uniformsql.network.packets.type.IntegerType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeStringType;
import cn.edu.bit.linc.uniformsql.network.packets.type.StringType;

/**
 * 错误响应报文
 */
public class ErrorPacket extends BasePacket {

    /**
     * 类型标识的偏移量
     */
    public final static int OFFSET_PACKET_IDENTIFIER = 0;

    /**
     * 类型标识的长度
     */
    public final static int LENGTH_PACKET_IDENTIFIER = 1;

    /**
     * 错误编号的偏移量
     */
    public final static int OFFSET_ERROR_CODE = 1;

    /**
     * 错误编号的长度
     */
    public final static int LENGTH_ERROR_CODE = 2;

    /**
     * 服务器状态的偏移量
     */
    public final static int OFFSET_SERVER_STATUS = 3;

    /**
     * 服务器状态的长度
     */
    public final static int LENGTH_SERVER_STATUS = 2;

    /**
     * 错误消息的偏移量
     */
    public final static int OFFSET_ERROR_MESSAGE = 5;

    /**
     * 创建指定大小的错误响应报文
     *
     * @param size 指定大小（字节为单位）
     */
    public ErrorPacket(int size) {
        super(size);
    }

    /**
     * 设置类型标识字段
     *
     * @param packetIdentifier 类型标识
     */
    public void setPacketIdentifier(IntegerType packetIdentifier) {
        byte[] bytes = new byte[packetIdentifier.getSize()];
        packetIdentifier.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_PACKET_IDENTIFIER, bytes.length);
    }

    /**
     * 获取类型标识字段
     *
     * @return 类型标识
     */
    public IntegerType getPacketIdentifier() {
        byte[] data = new byte[LENGTH_PACKET_IDENTIFIER];
        System.arraycopy(_data_, OFFSET_PACKET_IDENTIFIER, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置错误编号字段
     *
     * @param errorCode 错误编号
     */
    public void setErrorCode(IntegerType errorCode) {
        byte[] bytes = new byte[errorCode.getSize()];
        errorCode.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_ERROR_CODE, bytes.length);
    }

    /**
     * 获取错误编号字段
     *
     * @return 错误编号
     */
    public IntegerType getErrorCode() {
        byte[] data = new byte[LENGTH_ERROR_CODE];
        System.arraycopy(_data_, OFFSET_ERROR_CODE, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置服务器状态字段
     *
     * @param serverStatus 服务器状态
     */
    public void setServerStatus(IntegerType serverStatus) {
        byte[] bytes = new byte[serverStatus.getSize()];
        serverStatus.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_SERVER_STATUS, bytes.length);
    }

    /**
     * 获取服务器状态字段
     *
     * @return 服务器状态
     */
    public IntegerType getServerStatus() {
        byte[] data = new byte[LENGTH_SERVER_STATUS];
        System.arraycopy(_data_, OFFSET_SERVER_STATUS, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置错误消息字段
     *
     * @param errorMessage 错误消息
     */
    public void setErrorMessage(LengthCodeStringType errorMessage) {
        byte[] bytes = new byte[errorMessage.getSize()];
        errorMessage.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_ERROR_MESSAGE, bytes.length);
    }

    /**
     * 获取错误消息字段
     *
     * @return 错误消息
     */
    public LengthCodeStringType getErrorMessage() {
        byte[] data = new byte[_data_.length - OFFSET_ERROR_MESSAGE];
        System.arraycopy(_data_, OFFSET_ERROR_MESSAGE, data, 0, data.length);
        return LengthCodeStringType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {

        IntegerType packetIdentifier = IntegerType.getIntegerType(0xff, LENGTH_PACKET_IDENTIFIER);
        IntegerType errorCode = IntegerType.getIntegerType(0, LENGTH_ERROR_CODE);
        IntegerType serverStatus = IntegerType.getIntegerType(500, LENGTH_SERVER_STATUS);
        LengthCodeStringType errorMessage = LengthCodeStringType.getLengthCodeString("SERVER ERROR");

        ErrorPacket errorPacket = new ErrorPacket(packetIdentifier.getSize() + errorCode.getSize() + serverStatus.getSize() + errorMessage.getSize());
        errorPacket.setPacketIdentifier(packetIdentifier);
        errorPacket.setErrorCode(errorCode);
        errorPacket.setServerStatus(serverStatus);
        errorPacket.setErrorMessage(errorMessage);

        System.out.println(errorPacket);
        System.out.println("Packet Identifier : " + IntegerType.getIntegerValue(errorPacket.getPacketIdentifier()));
        System.out.println("Error Code        : " + IntegerType.getIntegerValue(errorPacket.getErrorCode()));
        System.out.println("Server Status     : " + IntegerType.getIntegerValue(errorPacket.getServerStatus()));
        System.out.println("Error Message     : " + LengthCodeStringType.getString(errorPacket.getErrorMessage()));

    }
    /* Output:
    [-1, 0, 0, 1, -12, 12, 83, 69, 82, 86, 69, 82, 32, 69, 82, 82, 79, 82]
    Packet Identifier : 255
    Error Code        : 0
    Server Status     : 500
    Error Message     : SERVER ERROR
     */

}
