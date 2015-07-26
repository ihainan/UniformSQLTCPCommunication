package cn.edu.bit.linc.uniformsql.network.packets;

import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeBinaryType;

/**
 * 结果集头部报文
 */
public class ResultHeadPacket extends BasePacket {

    /**
     * 域数量的偏移量
     */
    public final static int OFFSET_FIELD_NUMBER = 0;

    /**
     * 域数量的长度
     */
    private int fieldLen = -1;

    /**
     * 额外信息的偏移量 - 域数量的长度
     */
    public final static int OFFSET_EXTRA_MESSAGE_MINUS_FIELD_LENGTH = 0;

    /**
     * 额外信息的长度
     */
    private int extraLen = -1;

    /**
     * 创建指定大小的结果集头部报文
     *
     * @param size 指定大小（字节为单位）
     */
    public ResultHeadPacket(int size) {
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
     * 设置域数量字段
     *
     * @param fieldNumber 域数量
     */
    public void setFieldNumber(LengthCodeBinaryType fieldNumber) {
        byte[] bytes = new byte[fieldNumber.getSize()];
        fieldLen = fieldNumber.getSize();
        fieldNumber.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_FIELD_NUMBER, bytes.length);
    }

    /**
     * 获取域数量字段
     *
     * @return 域数量
     */
    public LengthCodeBinaryType getFieldNumber() {
        byte[] data = new byte[fieldLen];
        System.arraycopy(_data_, OFFSET_FIELD_NUMBER, data, 0, data.length);
        return LengthCodeBinaryType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 设置额外信息字段
     *
     * @param extraMessage 额外信息
     */
    public void setExtraMessage(LengthCodeBinaryType extraMessage) {
        byte[] bytes = new byte[extraMessage.getSize()];
        extraLen = extraMessage.getSize();
        extraMessage.getData(bytes);
        System.arraycopy(bytes, 0, _data_, fieldLen + OFFSET_EXTRA_MESSAGE_MINUS_FIELD_LENGTH, bytes.length);
    }

    /**
     * 获取额外信息字段
     *
     * @return 额外信息
     */
    public LengthCodeBinaryType getExtraMessage() {
        byte[] data = new byte[extraLen];
        System.arraycopy(_data_, fieldLen + OFFSET_EXTRA_MESSAGE_MINUS_FIELD_LENGTH, data, 0, data.length);
        return LengthCodeBinaryType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {

        LengthCodeBinaryType fieldNumber = LengthCodeBinaryType.getLengthCodeBinaryType(new byte[]{1, 2, 3, 4, 5, 6, 7, 8});
        LengthCodeBinaryType extraMessage = LengthCodeBinaryType.getLengthCodeBinaryType(new byte[]{4, 3, 2, 1});

        ResultHeadPacket resultHeadPacket = new ResultHeadPacket(fieldNumber.getSize() + extraMessage.getSize());
        resultHeadPacket.setFieldNumber(fieldNumber);
        resultHeadPacket.setExtraMessage(extraMessage);

        System.out.println(resultHeadPacket);
        System.out.print("Field Number      : ");
        byte[] bytes = LengthCodeBinaryType.getBytes(resultHeadPacket.getFieldNumber());
        for(byte b : bytes) {
            System.out.print(b + " ");
        }
        System.out.println();

        System.out.print("Extra Message     : ");
        bytes = LengthCodeBinaryType.getBytes(resultHeadPacket.getExtraMessage());
        for(byte b : bytes) {
            System.out.print(b + " ");
        }
        System.out.println();

    }
    /* Output:
    [8, 1, 2, 3, 4, 5, 6, 7, 8, 4, 4, 3, 2, 1]
    Field Number      : 1 2 3 4 5 6 7 8
    Extra Message     : 4 3 2 1
     */

}
