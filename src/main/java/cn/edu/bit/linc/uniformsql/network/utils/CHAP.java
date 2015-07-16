package cn.edu.bit.linc.uniformsql.network.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * CHAP 安全认证机制
 *
 * @Author ihainan
 */
public class CHAP {
    /**
     * 根据密码、挑战数计算得到挑战验证值
     *
     * @param password 密码
     * @param scramble 挑战数
     * @return 挑战验证值
     */
    public static String calcToken(String password, String scramble) {
        String stageOneHash = SHA1(password);
        String token = xor(SHA1(scramble + SHA1(stageOneHash)), stageOneHash);
        return token;
    }

    /**
     * 验证密码是否正确
     *
     * @param realPassword SHA1(SHA1(user.password)) 得到
     * @param scramble     挑战数
     * @param token        挑战验证值
     * @return 返回 <code>true</code> 表示密码正确，<code>false</code> 密码不正确
     */
    public static boolean checkToken(String realPassword, String scramble, String token) {
        String stageOneHash = xor(token, SHA1(scramble + realPassword));
        return SHA1(stageOneHash).equals(realPassword);
    }

    /**
     * 字符串 xor 计算
     *
     * @param str1 操作数1
     * @param str2 操作数2
     * @return xor 计算结果
     */
    private static String xor(String str1, String str2) {
        byte[] input = str1.getBytes();
        byte[] secret = str2.getBytes();

        final byte[] output = new byte[input.length];
        if (secret.length == 0) {
            throw new IllegalArgumentException("empty security key");
        }
        int spos = 0;
        for (int pos = 0; pos < input.length; ++pos) {
            output[pos] = (byte) (input[pos] ^ secret[spos]);
            ++spos;
            if (spos >= secret.length) {
                spos = 0;
            }
        }
        return new String(output);
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {
        String password = "12345";
        String scramble = "12345678901234567890";
        String realPassword = SHA1(SHA1("12345"));
        String token = calcToken(password, scramble);
        System.out.println(token.length());
        System.out.println(checkToken(realPassword, scramble, token));
    }

    /**
     * SHA1 加密
     *
     * @param password 需要加密的字符串
     * @return 加密后的字符串
     */
    private static String SHA1(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    /**
     * 字节数组转换为十六进制字符串
     *
     * @param hash 字节数组
     * @return 十六进制字符串
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
