package cn.edu.bit.linc.uniformsql.network.packets;

import java.util.Arrays;

/**
 * 所有数据包的基类，提供通用方法¬
 */
public abstract class BasePacket {
    /**
     * 原始包数据
     */
    protected byte[] _data_;

    /**
     * 创建指定大小的包头数据包
     *
     * @param size 指定大小（字节为单位）
     */
    public BasePacket(int size) {
        setData(new byte[size]);
    }

    /**
     * 设置原始 _data_ 数组为指定 byte 数组
     *
     * @param data 指定 byte 数组
     */
    public void setData(byte[] data) {
        _data_ = data;
    }

    /**
     * 获取 byte 数组
     *
     * @param data 希望被赋值的 byte 数组
     */
    public void getData(byte[] data) {
        System.arraycopy(_data_, 0, data, 0, data.length);
    }

    /**
     * 获取包大小
     *
     * @return 包大小（字节为单位）
     */
    public int getSize() {
        return _data_.length;
    }

    /**
     * 复制包，如果当前包大小与新包大小不一致，则新的大小与新包一致
     *
     * @param packet 需要被复制的包
     */
    public final void copy(BasePacket packet) {
        if (_data_.length != packet.getSize()) {
            setData(new byte[packet.getSize()]);
        }
        System.arraycopy(packet._data_, 0, _data_, 0, _data_.length);
    }

    /**
     * 字符串输出 byte 数组
     *
     * @return byte 数组的字符串表示
     */
    @Override
    public String toString() {
        return Arrays.toString(_data_);
    }


    /**
     * 赋值 byte 数组中的某几个连续元素
     *
     * @param bytes  需要赋值的 byte 数组
     * @param offset 起始位置在数组中的偏移量
     * @param length 元素的个数
     * @param data   需要赋值的整数值
     */
    public void setBytes(byte[] bytes, int offset, int length, int data) {
        for (int i = 0; i < length; ++i) {
            bytes[offset + i] = (byte) ((data >> ((length - i - 1) * 8)) & 0xff);
        }
    }

    /**
     * 将 byte 数组转换成整数并返回
     *
     * @param bytes  需要转换的 byte 数组
     * @param offset 起始位置在数组中的偏移量
     * @param length 转换的长度
     * @return 转换得到的整数
     */
    public int getIntFromBytes(byte[] bytes, int offset, int length) {
        int data = 0;
        for (int i = 0; i < length; ++i) {
            data += ((bytes[offset + i] & 0xff) << (8 * (length - i - 1)));
        }
        return data;
    }
}
