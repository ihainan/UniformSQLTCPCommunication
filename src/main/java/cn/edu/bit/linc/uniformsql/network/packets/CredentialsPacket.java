package cn.edu.bit.linc.uniformsql.network.packets;

/**
 * 客户端认证报文
 */
public class CredentialsPacket extends BasePacket {
    private final static int OFFSET_SERVER_CAPABILITIES = 0;
    private final static int OFFSET_MAX_PACKET_LENGTH = 4;
    private final static int OFFSET_CHARACTER_SET = 8;
    private final static int OFFSET_RESERVED_FIELD = 9;
    private final static int OFFSET_CREDENTIAL_INFO = 32;

    /**
     * 创建指定大小的包头数据包
     *
     * @param size 指定大小（字节为单位）
     */
    public CredentialsPacket(int size) {
        super(size);
    }

    /**
     * 设置客户端权能标志
     *
     * @param capabilities 客户端权能标志
     */
    public void setServerCapabilities(byte[] capabilities) {
        System.arraycopy(capabilities, 0, _data_, OFFSET_SERVER_CAPABILITIES, 4);
    }

    /**
     * 获取客户端权能标志
     *
     * @return 客户端权能标志
     */
    public byte[] getServerCapabilities() {
        byte[] capabilities = new byte[4];
        System.arraycopy(_data_, OFFSET_SERVER_CAPABILITIES, capabilities, 0, capabilities.length);
        return capabilities;
    }

    /**
     * 设置最大报文长度
     *
     * @param maxPacketLength 最大报文长度
     */
    public void setMaxPacketLength(int maxPacketLength) {
        setBytes(_data_, OFFSET_MAX_PACKET_LENGTH, 4, maxPacketLength);
    }

    /**
     * 获取最大报文长度
     *
     * @return 最大报文长度
     */
    public int getMaxPacketLength() {
        return getIntFromBytes(_data_, OFFSET_CHARACTER_SET, 4);
    }

    /**
     * 设置客户端字符集
     *
     * @param characterSet 客户端字符集
     */
    public void setCharacterSet(byte characterSet) {
        _data_[OFFSET_CHARACTER_SET] = characterSet;
    }

    /**
     * 获取客户端字符集
     *
     * @return 客户端字符集
     */
    public byte getCharacterSet() {
        return _data_[OFFSET_CHARACTER_SET];
    }

    
    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {
        byte[] capabilities = new byte[]{1, 2, 3, 4};
        int maxPacketLength = 0;    // 报文长度没有限制
        byte characterSet = 0;
    }

}
