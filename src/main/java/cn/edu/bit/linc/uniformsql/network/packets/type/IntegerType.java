package cn.edu.bit.linc.uniformsql.network.packets.type;

/**
 * 字段基本类型值 - 整数类型 <br>
 * 字段使用大端字节序 <br>
 */
public class IntegerType extends BasicType {
    /**
     * 构造函数，私有方法
     */
    protected IntegerType() {
    }

    /**
     * 获取 IntegerType 对应的整型值
     *
     * @param integerType IntegerType 实例
     * @return 对应的整数值
     */
    public static int getIntegerValue(IntegerType integerType) {
        byte[] data = new byte[integerType.getSize()];
        integerType.getData(data);
        int value = 0;
        for (int i = 0; i < data.length; ++i) {
            value += ((data[i] & 0xff) << (8 * (data.length - i - 1)));
        }
        return value;
    }

    /**
     * 整数转换成指定大小的 byte 数组
     *
     * @param bytes 希望得到的 byte 数组
     * @param size  byte 数组的大小
     * @param value 需要转换的整数
     */
    public static void fillByteArray(byte[] bytes, int size, int value) {
        for (int i = 0; i < size; ++i) {
            bytes[i] = (byte) ((value >> ((size - i - 1) * 8)) & 0xff);
        }
    }

    /**
     * 获取得到一个 IntegerType 实例
     *
     * @param value 整数值
     * @param size  数据占据的位数
     * @return IntegerType 实例
     */
    public static IntegerType getIntegerType(int value, int size) {
        byte[] data = new byte[size];
        fillByteArray(data, size, value);

        IntegerType integerType = new IntegerType();
        integerType.setData(data);
        return integerType;
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {
        // 构建 IntegerType 示例
        IntegerType integerType = IntegerType.getIntegerType(1256, 2);
        byte[] bytes = new byte[integerType.getSize()];
        integerType.getData(bytes);

        // 打印 byte 数组
        for (byte b : bytes) {
            System.out.print(String.format("0x%02x ", b));
        }
        System.out.println();
        System.out.println(getIntegerValue(integerType));
    }
    /* Output:
    0x04 0xe8
    1256
    */
}
