package cn.edu.bit.linc.uniformsql.network.packets;

import cn.edu.bit.linc.uniformsql.network.packets.type.IntegerType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeBinaryType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeStringType;

/**
 * 成功响应报文
 */
public class SuccessPacket extends BasePacket {

    /**
     * 类型标识的偏移量
     */
    public final static int OFFSET_PACKET_IDENTIFIER = 0;

    /**
     * 类型标识的长度
     */
    public final static int LENGTH_PACKET_IDENTIFIER = 1;

    /**
     * 改变行数的偏移量
     */
    public final static int OFFSET_CHANGED_ROWS = 1;

    /**
     * 改变行数的长度
     */
    private int rowLen = -1;

    /**
     * 索引ID的偏移量-改变行数的长度
     */
    public final static int OFFSET_INDEX_ID_MINUS_CR_LENGTH = 1;

    /**
     * 索引ID的长度
     */
    private int idLen = -1;

    /**
     * 服务器状态的偏移量-索引ID的长度
     */
    public final static int OFFSET_SERVER_STATUS_MINUS_ID_LENGTH = 1;

    /**
     * 服务器状态的长度
     */
    public final static int LENGTH_SERVER_STATUS = 2;

    /**
     * 警告次数的偏移量-索引ID的长度
     */
    public final static int OFFSET_WARNING_NUMBER_MINUS_ID_LENGTH = 3;

    /**
     * 警告次数的长度
     */
    public final static int LENGTH_WARNING_NUMBER = 2;

    /**
     * 服务器消息的偏移量-索引ID的长度
     */
    public final static int OFFSET_SERVER_MESSAGE_MINUS_ID_LENGTH = 5;

    /**
     * 创建指定大小的成功响应报文
     *
     * @param size 指定大小（字节为单位）
     */
    public SuccessPacket(int size) {
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
     * 设置改变行数字段
     *
     * @param changedRows 改变行数
     */
    public void setChangedRows(LengthCodeBinaryType changedRows) {
        byte[] bytes = new byte[changedRows.getSize()];
        rowLen = changedRows.getSize();
        changedRows.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_CHANGED_ROWS, bytes.length);
    }

    /**
     * 获取改变行数字段
     *
     * @return 改变行数
     */
    public LengthCodeBinaryType getChangedRows() {
        byte[] tmp = new byte[_data_.length - OFFSET_CHANGED_ROWS];
        System.arraycopy(_data_, OFFSET_CHANGED_ROWS, tmp, 0, tmp.length);
        rowLen = LengthCodeBinaryType.getLength(tmp);

        byte[] data = new byte[rowLen];
        System.arraycopy(_data_, OFFSET_CHANGED_ROWS, data, 0, data.length);
        return LengthCodeBinaryType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 设置索引ID字段
     *
     * @param indexID 索引ID
     */
    public void setIndexID(LengthCodeBinaryType indexID) {
        byte[] bytes = new byte[indexID.getSize()];
        idLen = indexID.getSize();
        indexID.getData(bytes);
        System.arraycopy(bytes, 0, _data_, rowLen + OFFSET_INDEX_ID_MINUS_CR_LENGTH, bytes.length);
    }

    /**
     * 获取索引ID字段
     *
     * @return 索引ID
     */
    public LengthCodeBinaryType getIndexID() {
        byte[] tmp = new byte[_data_.length - rowLen - OFFSET_INDEX_ID_MINUS_CR_LENGTH];
        System.arraycopy(_data_, rowLen + OFFSET_INDEX_ID_MINUS_CR_LENGTH, tmp, 0, tmp.length);
        idLen = LengthCodeBinaryType.getLength(tmp);

        byte[] data = new byte[idLen];
        System.arraycopy(_data_, rowLen + OFFSET_INDEX_ID_MINUS_CR_LENGTH, data, 0, data.length);
        return LengthCodeBinaryType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 设置服务器状态字段
     *
     * @param serverStatus 服务器状态
     */
    public void setServerStatus(IntegerType serverStatus) {
        byte[] bytes = new byte[serverStatus.getSize()];
        serverStatus.getData(bytes);
        System.arraycopy(bytes, 0, _data_, rowLen + idLen + OFFSET_SERVER_STATUS_MINUS_ID_LENGTH, bytes.length);
    }

    /**
     * 获取服务器状态字段
     *
     * @return 服务器状态
     */
    public IntegerType getServerStatus() {
        byte[] data = new byte[LENGTH_SERVER_STATUS];
        System.arraycopy(_data_, rowLen + idLen + OFFSET_SERVER_STATUS_MINUS_ID_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置警告次数字段
     *
     * @param warningNumber 警告次数
     */
    public void setWarningNumber(IntegerType warningNumber) {
        byte[] bytes = new byte[warningNumber.getSize()];
        warningNumber.getData(bytes);
        System.arraycopy(bytes, 0, _data_, rowLen + idLen + OFFSET_WARNING_NUMBER_MINUS_ID_LENGTH, bytes.length);
    }

    /**
     * 获取警告次数字段
     *
     * @return 警告次数
     */
    public IntegerType getWarningNumber() {
        byte[] data = new byte[LENGTH_WARNING_NUMBER];
        System.arraycopy(_data_, rowLen + idLen + OFFSET_WARNING_NUMBER_MINUS_ID_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置服务器消息字段
     *
     * @param serverMessage 服务器消息
     */
    public void setServerMessage(LengthCodeStringType serverMessage) {
        byte[] bytes = new byte[serverMessage.getSize()];
        serverMessage.getData(bytes);
        System.arraycopy(bytes, 0, _data_, rowLen + idLen + OFFSET_SERVER_MESSAGE_MINUS_ID_LENGTH, bytes.length);
    }

    /**
     * 获取服务器消息字段
     *
     * @return 服务器消息
     */
    public LengthCodeStringType getServerMessage() {
        byte[] data = new byte[_data_.length - OFFSET_SERVER_MESSAGE_MINUS_ID_LENGTH - rowLen - idLen];
        System.arraycopy(_data_, rowLen + idLen + OFFSET_SERVER_MESSAGE_MINUS_ID_LENGTH, data, 0, data.length);
        return LengthCodeStringType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) throws PacketExceptions.NecessaryFieldNotSetException {

        IntegerType packetIdentifier = IntegerType.getIntegerType(0x00, LENGTH_PACKET_IDENTIFIER);
        LengthCodeBinaryType changedRows = LengthCodeBinaryType.getLengthCodeBinaryType(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        LengthCodeBinaryType indexID = LengthCodeBinaryType.getLengthCodeBinaryType(new byte[] {4, 3, 2, 1});
        IntegerType serverStatus = IntegerType.getIntegerType(500, LENGTH_SERVER_STATUS);
        IntegerType warningNumber = IntegerType.getIntegerType(2, LENGTH_WARNING_NUMBER);
        LengthCodeStringType serverMessage = LengthCodeStringType.getLengthCodeString("SERVER MESSAGE!");

        SuccessPacket successPacket = new SuccessPacket(packetIdentifier.getSize() + changedRows.getSize() + indexID.getSize() + serverStatus.getSize() + warningNumber.getSize() + serverMessage.getSize());
        successPacket.setPacketIdentifier(packetIdentifier);
        successPacket.setChangedRows(changedRows);
        successPacket.setIndexID(indexID);
        successPacket.setServerStatus(serverStatus);
        successPacket.setWarningNumber(warningNumber);
        successPacket.setServerMessage(serverMessage);

        System.out.println(successPacket);
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

        /* 通过byte[]构建 */
        SuccessPacket successPacketCopy = new SuccessPacket(successPacket.getSize());
        byte[] data = new byte[successPacket.getSize()];
        successPacket.getData(data);
        successPacketCopy.setData(data);
        System.out.println();

        System.out.println(successPacketCopy);
        System.out.println("Packet Identifier : " + IntegerType.getIntegerValue(successPacketCopy.getPacketIdentifier()));
        System.out.print("Changed Rows      : ");
        bytes = LengthCodeBinaryType.getBytes(successPacketCopy.getChangedRows());
        for(byte b : bytes) {
            System.out.print(b + " ");
        }
        System.out.println();

        System.out.print("Index ID          : ");
        bytes = LengthCodeBinaryType.getBytes(successPacketCopy.getIndexID());
        for(byte b : bytes) {
            System.out.print(b + " ");
        }
        System.out.println();

        System.out.println("Server Status     : " + IntegerType.getIntegerValue(successPacketCopy.getServerStatus()));
        System.out.println("Warning Number    : " + IntegerType.getIntegerValue(successPacketCopy.getWarningNumber()));
        System.out.println("Server Message    : " + LengthCodeStringType.getString(successPacketCopy.getServerMessage()));

    }
    /* Output:
    [0, 8, 1, 2, 3, 4, 5, 6, 7, 8, 4, 4, 3, 2, 1, 1, -12, 0, 2, 15, 83, 69, 82, 86, 69, 82, 32, 77, 69, 83, 83, 65, 71, 69, 33]
    Packet Identifier : 0
    Changed Rows      : 1 2 3 4 5 6 7 8
    Index ID          : 4 3 2 1
    Server Status     : 500
    Warning Number    : 2
    Server Message    : SERVER MESSAGE!
     */

}
