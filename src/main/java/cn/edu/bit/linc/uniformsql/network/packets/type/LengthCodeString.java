package cn.edu.bit.linc.uniformsql.network.packets.type;

/**
 * 字段基本类型值 - 长度编码字符串
 */
public class LengthCodeString extends LengthCodeBinaryType {
    /**
     * 构造函数
     */
    protected LengthCodeString() {
        super();
    }

    /**
     * 获取 LengthCodeString 的实例
     *
     * @param str 字符串
     * @return LengthCodeString 的实例
     */
    public static LengthCodeString getLengthCodeString(String str) {
        byte[] bytes = str.getBytes();
        LengthCodeBinaryType lengthCodeBinaryType = getLengthCodeBinaryType(bytes);

        byte[] data = new byte[lengthCodeBinaryType.getSize()];
        lengthCodeBinaryType.getData(data);

        LengthCodeString lengthCodeString = new LengthCodeString();
        lengthCodeString.setData(data);

        return lengthCodeString;
    }

    /**
     * 获取 LengthCodeString 中存储的字符串
     *
     * @param lengthCodeString LengthCodeString 实例
     * @return LengthCodeString 中存储的字符串
     */
    public static String getString(LengthCodeString lengthCodeString) {
        byte[] bytes = getBytes(lengthCodeString);
        return new String(bytes);
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {
        String str = "Hello World";
        LengthCodeString lengthCodeString = getLengthCodeString(str);
        System.out.println(getString(lengthCodeString));
    }
    /* Output:
       Hello World
     */
}
