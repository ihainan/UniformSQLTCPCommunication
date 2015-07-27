package cn.edu.bit.linc.uniformsql.network.packets.type;

/**
 * 字段基本类型值 - 长度编码二进制数据类型
 */
public class LengthCodeBinaryType extends BasicType {
    /**
     * 构造函数，私有方法
     */
    protected LengthCodeBinaryType() {
    }

    /**
     * 获取 LengthCodeBinaryType 的实例
     *
     * @param bytes byte 数组
     * @return LengthCodeBinaryType 的实例
     */
    public static LengthCodeBinaryType getLengthCodeBinaryType(byte[] bytes) {
        // TODO: 考虑字节序问题！！！！
        // TODO: 考虑长度数值太大的问题！！！
        // TODO: 考虑有符号和无符号 Byte 的问题！！！！
        /* 长度字段 */
        long length = bytes.length;
        byte[] lengthBytes;
        if (length == 0) {
            // 1 字节表示长度为 0
            lengthBytes = new byte[]{(byte) 251};
        } else if (length < 251) {
            // 1 字节表示长度
            lengthBytes = new byte[]{(byte) length};
        } else if (length < 65536) {
            // 3 字节表示长度
            lengthBytes = new byte[]{
                    (byte) ((length >> 16) & 0xFF),
                    (byte) ((length >> 8) & 0xFF),
                    (byte) ((length) & 0xFF)};
        } else if (length < 16777216) {
            lengthBytes = new byte[]{
                    (byte) ((length >> 24) & 0xFF),
                    (byte) ((length >> 16) & 0xFF),
                    (byte) ((length >> 8) & 0xFF),
                    (byte) ((length) & 0xFF)};
        } else {
            lengthBytes = new byte[]{
                    (byte) ((length >> 64) & 0xFF),
                    (byte) ((length >> 56) & 0xFF),
                    (byte) ((length >> 48) & 0xFF),
                    (byte) ((length >> 40) & 0xFF),
                    (byte) ((length >> 32) & 0xFF),
                    (byte) ((length >> 24) & 0xFF),
                    (byte) ((length >> 16) & 0xFF),
                    (byte) ((length >> 8) & 0xFF),
                    (byte) ((length) & 0xFF)};
        }

        /* 构建 Byte 数组 */
        byte[] data = new byte[(int) (length + lengthBytes.length)];
        System.arraycopy(lengthBytes, 0, data, 0, lengthBytes.length);
        System.arraycopy(bytes, 0, data, lengthBytes.length, bytes.length);

        /* 包装 */
        LengthCodeBinaryType lengthCodeBinaryType = new LengthCodeBinaryType();
        lengthCodeBinaryType.setData(data);

        return lengthCodeBinaryType;
    }

    /**
     * 使用 data 数组构建达到一个 LengthCodeBinaryType 实例
     *
     * @param data 原始数据（含长度字段）
     * @return LengthCodeBinaryType 实例
     */
    public static LengthCodeBinaryType getLengthCodeBinaryTypeUsingData(byte[] data) {
        LengthCodeBinaryType lengthCodeBinaryType = new LengthCodeBinaryType();
        lengthCodeBinaryType.setData(data);
        return lengthCodeBinaryType;
    }

    /**
     * 获取 LengthCodeBinaryType 中存储的 byte 数组
     *
     * @param lengthCodeBinaryType LengthCodeBinaryType 实例
     * @return byte 数组
     */
    public static byte[] getBytes(LengthCodeBinaryType lengthCodeBinaryType) {
        // TODO: 考虑字节序问题！！！！
        byte[] data = new byte[lengthCodeBinaryType.getSize()];
        lengthCodeBinaryType.getData(data);

        int startPos = -1, length = 0;
        if ((data[0] & 0xFF) < 251) {
            startPos = 1;
            length = data[0] & 0xFF;
        } else if ((data[0] & 0xFF) == 251) {
            startPos = -1;
            length = 0;
        } else if ((data[0] & 0xFF) == 253) {
            startPos = 3;
            length = ((data[0] << 16) & 0xFF)
                    + ((data[1] << 8) & 0xFF)
                    + (data[2] & 0xFF);
        } else if ((data[0] & 0xFF) == 254) {
            startPos = 9;
            length = ((data[0] & 0xFF) << 64)
                    + ((data[1] & 0xFF) << 56)
                    + ((data[2] & 0xFF) << 48)
                    + ((data[3] & 0xFF) << 40)
                    + ((data[4] & 0xFF) << 32)
                    + ((data[5] & 0xFF) << 24)
                    + ((data[6] & 0xFF) << 16)
                    + ((data[7] & 0xFF) << 8)
                    + (data[8] & 0xFF);
        }

        if (length == 0) return null;
        else {
            byte[] bytes = new byte[length];
            System.arraycopy(data, startPos, bytes, 0, length);
            return bytes;
        }
    }

    /**
     * 获取 byte 数组中存储的 length
     *
     * @param data byte 数组
     * @return length 长度
     */
    public static int getLength(byte[] data) {
        // TODO: 考虑字节序问题！！！！

        int startPos = -1, length = 0;
        if ((data[0] & 0xFF) < 251) {
            startPos = 1;
            length = data[0] & 0xFF;
        } else if ((data[0] & 0xFF) == 251) {
            startPos = -1;
            length = 0;
        } else if ((data[0] & 0xFF) == 253) {
            startPos = 3;
            length = ((data[0] << 16) & 0xFF)
                    + ((data[1] << 8) & 0xFF)
                    + (data[2] & 0xFF);
        } else if ((data[0] & 0xFF) == 254) {
            startPos = 9;
            length = ((data[0] & 0xFF) << 64)
                    + ((data[1] & 0xFF) << 56)
                    + ((data[2] & 0xFF) << 48)
                    + ((data[3] & 0xFF) << 40)
                    + ((data[4] & 0xFF) << 32)
                    + ((data[5] & 0xFF) << 24)
                    + ((data[6] & 0xFF) << 16)
                    + ((data[7] & 0xFF) << 8)
                    + (data[8] & 0xFF);
        }

        return length + startPos;
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {
        /* 测试一 */
        byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        LengthCodeBinaryType lengthCodeBinaryType = LengthCodeBinaryType.getLengthCodeBinaryType(bytes);

        // 输出
        byte[] data = new byte[lengthCodeBinaryType.getSize()];
        lengthCodeBinaryType.getData(data);

        // 打印 byte 数组
        for (byte b : data) {
            System.out.print(String.format("0x%02x ", b));
        }
        System.out.println();
        System.out.println(data.length);
        System.out.println(getLength(data));

        for (byte b : getBytes(lengthCodeBinaryType)) {
            System.out.print(String.format("0x%02x ", b));
        }
        System.out.println();

        /* 测试二 */
        System.out.println();
        bytes = new byte[]{};
        lengthCodeBinaryType = LengthCodeBinaryType.getLengthCodeBinaryType(bytes);

        // 输出
        data = new byte[lengthCodeBinaryType.getSize()];
        lengthCodeBinaryType.getData(data);

        // 打印 byte 数组
        for (byte b : data) {
            System.out.print(String.format("0x%02x ", b));
        }
        System.out.println();
        System.out.println(data.length);
    }
    /* Output:
    0x08 0x01 0x02 0x03 0x04 0x05 0x06 0x07 0x08
    9
    0x01 0x02 0x03 0x04 0x05 0x06 0x07 0x08

    0xfb
    1
     */

}