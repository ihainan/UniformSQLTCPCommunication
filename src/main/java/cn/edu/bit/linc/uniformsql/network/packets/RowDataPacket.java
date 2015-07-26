package cn.edu.bit.linc.uniformsql.network.packets;

import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeStringType;

/**
 * 行数据报文
 */
public class RowDataPacket extends BasePacket {

    /**
     * 偏移量数组
     */
    private int[] rowOffsets;

    /**
     * 创建指定大小的行数据报文
     *
     * @param size 指定大小（字节为单位）
     */
    public RowDataPacket(int size) {
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
     * 设置行数据字段
     *
     * @param rowDataArray 行数据数组
     */
    public void setRowData(LengthCodeStringType[] rowDataArray) {
        rowOffsets = new int[rowDataArray.length+1];
        rowOffsets[0] = 0;
        for(int index = 0; index < rowDataArray.length; ++index) {
            byte[] bytes = new byte[rowDataArray[index].getSize()];
            rowDataArray[index].getData(bytes);
            System.arraycopy(bytes, 0, _data_, rowOffsets[index], bytes.length);
            rowOffsets[index+1] = rowOffsets[index] + bytes.length;
        }
    }

    /**
     * 获取行数据字段
     *
     * @return 行数据数组
     */
    public LengthCodeStringType[] getRowData() {
        LengthCodeStringType[] rowDataArray = new LengthCodeStringType[rowOffsets.length-1];

        for(int index = 0; index < rowDataArray.length; ++index) {
            rowDataArray[index] = getRowDataByIndex(index);
        }
        return rowDataArray;
    }

    /**
     * 获取行数据字段
     *
     * @param index 下标
     * @return 行数据
     */
    public LengthCodeStringType getRowDataByIndex(int index) {
        byte[] data = new byte[rowOffsets[index+1] - rowOffsets[index]];
        System.arraycopy(_data_, rowOffsets[index], data, 0, data.length);
        return LengthCodeStringType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {

        LengthCodeStringType[] rowDataArray = new LengthCodeStringType[] {LengthCodeStringType.getLengthCodeString("111"), LengthCodeStringType.getLengthCodeString("222"), LengthCodeStringType.getLengthCodeString("333")};

        RowDataPacket rowDataPacket = new RowDataPacket(12);
        rowDataPacket.setRowData(rowDataArray);

        System.out.println(rowDataPacket);

        LengthCodeStringType[] rowDataArrayRes = rowDataPacket.getRowData();
        for(LengthCodeStringType rowData : rowDataArrayRes) {
            System.out.print(LengthCodeStringType.getString(rowData) + " ");
        }
        System.out.println();

        System.out.println(LengthCodeStringType.getString(rowDataPacket.getRowDataByIndex(0)));
        System.out.println(LengthCodeStringType.getString(rowDataPacket.getRowDataByIndex(1)));
        System.out.println(LengthCodeStringType.getString(rowDataPacket.getRowDataByIndex(2)));

    }
    /* Output:
    [3, 49, 49, 49, 3, 50, 50, 50, 3, 51, 51, 51]
    111 222 333
    111
    222
    333
     */

}
