package cn.edu.bit.linc.uniformsql.network.packets;

/**
 * 异常
 */
public class PacketExceptions {
    /**
     * 字段赋值超过了字段本身的大小
     */
    public static class ExceedFieldSizeException extends Exception{}

    /**
     * 必要字段没有设置
     */
    public static class NecessaryFieldNotSetException extends Exception{}
}
