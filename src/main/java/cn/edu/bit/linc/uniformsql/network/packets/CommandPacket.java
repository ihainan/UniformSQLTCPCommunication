package cn.edu.bit.linc.uniformsql.network.packets;

import cn.edu.bit.linc.uniformsql.network.packets.type.IntegerType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeStringType;

/**
 * 命令请求报文
 */
public class CommandPacket extends BasePacket {

    /**
     * 命令代码的偏移量
     */
    public final static int OFFSET_COMMAND_CODE = 0;

    /**
     * 命令代码的长度
     */
    public final static int LENGTH_COMMAND_CODE = 1;

    /**
     * 命令内容的偏移量
     */
    public final static int OFFSET_COMMAND = 1;

    /**
     * 创建指定大小的命令请求报文
     *
     * @param size 指定大小（字节为单位）
     */
    public CommandPacket(int size) {
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
     * 设置命令编码字段
     *
     * @param commandCode 命令编码
     */
    public void setCommandCode(IntegerType commandCode) {
        byte[] bytes = new byte[commandCode.getSize()];
        commandCode.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_COMMAND_CODE, bytes.length);
    }

    /**
     * 获取命令编码字段
     *
     * @return 命令编码
     */
    public IntegerType getCommandCode() {
        byte[] data = new byte[LENGTH_COMMAND_CODE];
        System.arraycopy(_data_, OFFSET_COMMAND_CODE, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置命令内容字段
     *
     * @param command 命令内容
     */
    public void setCommand(LengthCodeStringType command) {
        byte[] bytes = new byte[command.getSize()];
        command.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_COMMAND, bytes.length);
    }

    /**
     * 获取命令内容字段
     *
     * @return 命令内容
     */
    public LengthCodeStringType getCommand() {
        byte[] data = new byte[_data_.length - OFFSET_COMMAND];
        System.arraycopy(_data_, OFFSET_COMMAND, data, 0, data.length);
        return LengthCodeStringType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {

        IntegerType commandCode = IntegerType.getIntegerType(1, LENGTH_COMMAND_CODE);
        LengthCodeStringType command = LengthCodeStringType.getLengthCodeString("SELECT * FROM `Student`");

        CommandPacket commandPacket = new CommandPacket(commandCode.getSize() + command.getSize());
        commandPacket.setCommandCode(commandCode);
        commandPacket.setCommand(command);

        System.out.println(commandPacket);
        System.out.println("Command Code      : " + IntegerType.getIntegerValue(commandPacket.getCommandCode()));
        System.out.println("Command           : " + LengthCodeStringType.getString(commandPacket.getCommand()));

    }

    /* Output:
    [1, 23, 83, 69, 76, 69, 67, 84, 32, 42, 32, 70, 82, 79, 77, 32, 96, 83, 116, 117, 100, 101, 110, 116, 96]
    Command Code      : 1
    Command           : SELECT * FROM `Student`
     */

}
