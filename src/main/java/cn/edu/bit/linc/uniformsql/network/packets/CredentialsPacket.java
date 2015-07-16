package cn.edu.bit.linc.uniformsql.network.packets;

import cn.edu.bit.linc.uniformsql.network.packets.type.IntegerType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeBinaryType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeString;
import cn.edu.bit.linc.uniformsql.network.packets.type.StringType;
import cn.edu.bit.linc.uniformsql.network.utils.CHAP;

import java.util.Arrays;

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
    public void setServerCapabilities(IntegerType capabilities) {
        byte[] bytes = new byte[capabilities.getSize()];
        capabilities.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_SERVER_CAPABILITIES, bytes.length);
    }

    /**
     * 获取客户端权能标志
     *
     * @return 客户端权能标志
     */
    public IntegerType getServerCapabilities() {
        byte[] data = new byte[4];
        System.arraycopy(_data_, OFFSET_SERVER_CAPABILITIES, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置最大报文长度
     *
     * @param maxPacketLength 最大报文长度
     */
    public void setMaxPacketLength(IntegerType maxPacketLength) {
        byte[] bytes = new byte[maxPacketLength.getSize()];
        maxPacketLength.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_MAX_PACKET_LENGTH, bytes.length);
    }

    /**
     * 获取最大报文长度
     *
     * @return 最大报文长度
     */
    public IntegerType getMaxPacketLength() {
        byte[] data = new byte[4];
        System.arraycopy(_data_, OFFSET_MAX_PACKET_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
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
     * 设置认证信息字段
     *
     * @param credentialInformation 认证信息
     */
    public void setCredentialInformation(CredentialInformation credentialInformation) {
        // 用户名
        byte[] bytes = credentialInformation.getBytesData();
        System.arraycopy(bytes, 0, _data_, OFFSET_CREDENTIAL_INFO, bytes.length);
    }

    /**
     * 获取认证信息字段
     *
     * @return 认证信息字段
     */
    public CredentialInformation getCredentialInformation() {
        byte[] bytes = new byte[_data_.length - OFFSET_CREDENTIAL_INFO];
        System.arraycopy(_data_, OFFSET_CREDENTIAL_INFO, bytes, 0, bytes.length);
        return CredentialInformation.getCredentialInformationFromBytes(bytes);
    }

    public static class CredentialInformation {
        public StringType userName;
        public LengthCodeString token;
        public StringType dbName;

        /**
         * 构造函数
         *
         * @param userName 用户名
         * @param token    挑战认证数据
         * @param dbName   连接数据库名
         */
        public CredentialInformation(StringType userName, LengthCodeString token, StringType dbName) {
            this.userName = userName;
            this.token = token;
            this.dbName = dbName;
        }

        /**
         * 获取三个字段合在一起的 byte 数组
         *
         * @return byte 数组
         */
        public byte[] getBytesData() {
            byte[] data = new byte[userName.getSize() + token.getSize() + ((dbName == null || dbName.getSize() == 0) ? 0 : dbName.getSize())];

            // 用户名
            byte[] bytesUserName = new byte[userName.getSize()];
            userName.getData(bytesUserName);
            System.arraycopy(bytesUserName, 0, data, 0, bytesUserName.length);

            // 挑战认证数据
            byte[] bytesToken = new byte[token.getSize()];
            token.getData(bytesToken);
            System.arraycopy(bytesToken, 0, data, bytesUserName.length, bytesToken.length);


            // 数据库名
            byte[] bytesDbName;
            if (dbName != null && dbName.getSize() != 0) {
                bytesDbName = new byte[dbName.getSize()];
                dbName.getData(bytesDbName);
                System.arraycopy(bytesDbName, 0, data, bytesUserName.length + bytesToken.length, bytesDbName.length);
            }

            return data;
        }

        /**
         * 从原始数据中提取 CredentialInformation 实例
         *
         * @param data byte 数组
         * @return CredentialInformation 实例
         */
        public static CredentialInformation getCredentialInformationFromBytes(byte[] data) {
            /* 寻找第一个 Null 符号在数组中的位置 */
            int posOfFirstNull = -1;
            for (int i = 0; i < data.length; ++i) {
                if (data[i] == 0x00) {
                    posOfFirstNull = i;
                    break;
                }
            }
            if (posOfFirstNull == -1) return null;

            // 用户名
            byte[] bytesUserName = new byte[posOfFirstNull + 1];
            System.arraycopy(data, 0, bytesUserName, 0, bytesUserName.length);
            StringType userName = StringType.getStringType(bytesUserName);

            // 认证数据
            byte[] bytesToken = new byte[41];
            System.arraycopy(data, posOfFirstNull + 1, bytesToken, 0, bytesToken.length);
            LengthCodeString token = LengthCodeString.getLengthCodeBinaryTypeUsingData(bytesToken);

            // 数据库名
            StringType dbName = StringType.getStringType("");
            if (userName.getSize() + token.getSize() != data.length) {
                byte[] bytesDbName = new byte[data.length - (userName.getSize() + token.getSize())];
                System.arraycopy(data, posOfFirstNull + 42, bytesDbName, 0, bytesDbName.length);
                dbName = StringType.getStringType(bytesDbName);
            }

            return new CredentialInformation(userName, token, dbName);
        }
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {
        /* 获取各字段数据 */
        IntegerType capabilities = IntegerType.getIntegerType(5, 4);
        IntegerType maxPacketLength = IntegerType.getIntegerType(10, 4);
        byte characterSet = 1;
        StringType userName = StringType.getStringType("ihainan");
        String password = "12345";
        String scramble = "12345678901234567890";
        String tokenStr = CHAP.calcToken(password, scramble);
        LengthCodeString token = LengthCodeString.getLengthCodeString(tokenStr);
        StringType dbName = StringType.getStringType("db_test");
        CredentialInformation credentialInformation = new CredentialInformation(userName, token, dbName);

        /* 构造验证包 */
        CredentialsPacket credentialsPacket = new CredentialsPacket(32 + userName.getSize() + token.getSize() + dbName.getSize());
        credentialsPacket.setServerCapabilities(capabilities);
        credentialsPacket.setMaxPacketLength(maxPacketLength);
        credentialsPacket.setCharacterSet(characterSet);
        credentialsPacket.setCredentialInformation(credentialInformation);

        /* 输出验证 */
        System.out.println("Character Set       : " + credentialsPacket.getCharacterSet());
        System.out.println("Max Packet Length   : " + IntegerType.getIntegerValue(credentialsPacket.getMaxPacketLength()));
        System.out.println("Server Capabilities : " + IntegerType.getIntegerValue(credentialsPacket.getServerCapabilities()));
        CredentialInformation credentialInformation1 = credentialsPacket.getCredentialInformation();
        System.out.println("User Name           : " + StringType.getString(credentialInformation1.userName));
        System.out.println("Token               : " + LengthCodeString.getString(credentialInformation1.token));
        System.out.println("Database Name       : " + StringType.getString(credentialInformation1.dbName));
    }
}
