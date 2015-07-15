package cn.edu.bit.linc.uniformsql.network.packets.type;

/**
 * 字段基本类型值 - 字符串类型
 */
public class StringType extends BasicType {
    /**
     * 构造函数，私有方法
     */
    protected StringType() {
    }

    /**
     * 获取一个 StringType 实例
     *
     * @param str 存储的字符串
     * @return StringType 实例
     */
    public static StringType getStringType(String str) {
        StringType stringType = new StringType();
        byte[] bytes = str.getBytes();

        // 最后的 NULL 结束符
        byte[] bytesWithNull = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, bytesWithNull, 0, bytes.length);
        bytesWithNull[bytesWithNull.length - 1] = 0x00;

        // 填充
        stringType.setData(bytesWithNull);
        return stringType;
    }

    /**
     * 获取 StringType 中存储的字符串
     *
     * @param stringType StringType 实例
     * @return
     */
    public static String getString(StringType stringType) {
        byte[] bytes = new byte[stringType.getSize()];
        byte[] bytesWithoutNull = new byte[bytes.length - 1];
        System.arraycopy(bytes, 0, bytesWithoutNull, 0, bytesWithoutNull.length);
        stringType.getData(bytesWithoutNull);
        return new String(bytesWithoutNull);
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {
        StringType stringType = StringType.getStringType("Hello World");
        System.out.println(getString(stringType));
    }
    /*output:
    Hello World
     */
}
