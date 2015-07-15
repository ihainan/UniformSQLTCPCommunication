package cn.edu.bit.linc.uniformsql.network.packets.type;

/**
 * 所有字段数据类型的基类
 */
public abstract class BasicType {
    /**
     * 原始数据
     */
    protected byte[] _data_;

    /**
     * 构造函数
     */
    protected BasicType() {}

    /**
     * 构造函数
     * @param size 数据大小
     */
    protected BasicType(int size) {
        _data_ = new byte[size];
    }

    /**
     * 获取数据所占据的字节数
     *
     * @return 数据所占据的字节数
     */
    public int getSize() {
        return _data_.length;
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
}
