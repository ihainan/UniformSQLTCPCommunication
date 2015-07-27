package cn.edu.bit.linc.uniformsql.network.packets;

import cn.edu.bit.linc.uniformsql.network.packets.type.IntegerType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeBinaryType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeStringType;

/**
 * 结果集报文
 */
public class ResultSetPacket extends BasePacket {

    /**
     * 结果集报文头长度
     */
    private int resultHeadPacketLen = -1;

    /**
     * 域描述报文偏移量数组
     */
    private int[] fieldPacketOffset;

    /**
     * EOF报文1偏移量
     */
    private int eofPacketOffset1 = -1;

    /**
     * EOF报文1长度
     */
    private int eofPacketLen1 = -1;

    /**
     * 行数据报文偏移量数组
     */
    private int[] rowDataPacketOffset;

    /**
     * EOF报文2偏移量
     */
    private int eofPacketOffset2 = -1;

    /**
     * EOF报文2长度
     */
    private int eofPacketLen2 = -1;

    /**
     * 创建指定大小的结果集报文
     *
     * @param size 指定大小（字节为单位）
     */
    public ResultSetPacket(int size) {
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
     * 设置结果集报文头
     *
     * @param resultHeadPacket 结果集报文头
     */
    public void setResultHeadPacket(ResultHeadPacket resultHeadPacket) {
        byte[] bytes = new byte[resultHeadPacket.getSize()];
        resultHeadPacket.getData(bytes);
        resultHeadPacketLen = bytes.length;
        System.arraycopy(bytes, 0, _data_, 0, bytes.length);
    }

    /**
     * 获取结果集报文头
     *
     * @return 结果集报文头
     */
    public ResultHeadPacket getResultHeadPacket() {
        byte[] data = new byte[resultHeadPacketLen];
        System.arraycopy(_data_, 0, data, 0, data.length);
        ResultHeadPacket resultHeadPacket = new ResultHeadPacket(data.length);
        resultHeadPacket.setData(data);
        return resultHeadPacket;
    }

    /**
     * 设置域描述报文
     *
     * @param fieldPacketArray 域描述报文数组
     */
    public void setFieldPacketArray(FieldPacket[] fieldPacketArray) {
        fieldPacketOffset = new int[fieldPacketArray.length+1];
        fieldPacketOffset[0] = resultHeadPacketLen;
        for(int index = 0; index < fieldPacketArray.length; ++index) {
            byte[] bytes = new byte[fieldPacketArray[index].getSize()];
            fieldPacketArray[index].getData(bytes);
            fieldPacketOffset[index+1] = fieldPacketOffset[index] + bytes.length;
            System.arraycopy(bytes, 0, _data_, fieldPacketOffset[index], bytes.length);
        }
    }

    /**
     * 获取域描述报文
     *
     * @return 域描述报文
     */
    public FieldPacket[] getFieldPacketArray() {
        FieldPacket[] fieldPacketArray = new FieldPacket[fieldPacketOffset.length-1];

        for(int index = 0; index < fieldPacketArray.length; ++index) {
            byte[] data = new byte[fieldPacketOffset[index+1] - fieldPacketOffset[index]];
            System.arraycopy(_data_, fieldPacketOffset[index], data, 0, data.length);
            FieldPacket fieldPacket = new FieldPacket(data.length);
            fieldPacket.setData(data);
            fieldPacketArray[index] = fieldPacket;
        }
        return fieldPacketArray;
    }

    /**
     * 设置EOF报文1
     *
     * @param eofPacket EOF报文1
     */
    public void setEOFPacket1(EOFPacket eofPacket) {
        byte[] bytes = new byte[eofPacket.getSize()];
        eofPacket.getData(bytes);
        eofPacketLen1 = bytes.length;
        eofPacketOffset1 = fieldPacketOffset[fieldPacketOffset.length-1];
        System.arraycopy(bytes, 0, _data_, eofPacketOffset1, bytes.length);
    }

    /**
     * 获取EOF报文1
     *
     * @return EOF报文1
     */
    public EOFPacket getEOFPacket1() {
        byte[] data = new byte[eofPacketLen1];
        System.arraycopy(_data_, eofPacketOffset1, data, 0, data.length);
        EOFPacket eofPacket = new EOFPacket(data.length);
        eofPacket.setData(data);
        return eofPacket;
    }

    /**
     * 设置行数据报文
     *
     * @param rowDataPacketArray 行数据报文数组
     */
    public void setRowDataPacketArray(RowDataPacket[] rowDataPacketArray) {
        rowDataPacketOffset = new int[rowDataPacketArray.length+1];
        rowDataPacketOffset[0] = eofPacketOffset1 + eofPacketLen1;
        for(int index = 0; index < rowDataPacketArray.length; ++index) {
            byte[] bytes = new byte[rowDataPacketArray[index].getSize()];
            rowDataPacketArray[index].getData(bytes);
            rowDataPacketOffset[index+1] = rowDataPacketOffset[index] + bytes.length;
            System.arraycopy(bytes, 0, _data_, rowDataPacketOffset[index], bytes.length);
        }
    }

    /**
     * 获取行数据报文
     *
     * @return 行数据报文
     */
    public RowDataPacket[] getRowDataPacketArray() {
        RowDataPacket[] rowDataPacketArray = new RowDataPacket[rowDataPacketOffset.length-1];

        for(int index = 0; index < rowDataPacketArray.length; ++index) {
            byte[] data = new byte[rowDataPacketOffset[index+1] - rowDataPacketOffset[index]];
            System.arraycopy(_data_, rowDataPacketOffset[index], data, 0, data.length);
            RowDataPacket rowDataPacket = new RowDataPacket(data.length);
            rowDataPacket.setData(data);
            rowDataPacketArray[index] = rowDataPacket;
        }
        return rowDataPacketArray;
    }

    /**
     * 设置EOF报文2
     *
     * @param eofPacket EOF报文2
     */
    public void setEOFPacket2(EOFPacket eofPacket) {
        byte[] bytes = new byte[eofPacket.getSize()];
        eofPacket.getData(bytes);
        eofPacketLen2 = bytes.length;
        eofPacketOffset2 = rowDataPacketOffset[rowDataPacketOffset.length-1];
        System.arraycopy(bytes, 0, _data_, eofPacketOffset2, bytes.length);
    }

    /**
     * 获取EOF报文1
     *
     * @return EOF报文1
     */
    public EOFPacket getEOFPacket2() {
        byte[] data = new byte[eofPacketLen2];
        System.arraycopy(_data_, eofPacketOffset2, data, 0, data.length);
        EOFPacket eofPacket = new EOFPacket(data.length);
        eofPacket.setData(data);
        return eofPacket;
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) {

        /* 结果头报文 */
        LengthCodeBinaryType fieldNumber = LengthCodeBinaryType.getLengthCodeBinaryType(new byte[]{1, 2});
        LengthCodeBinaryType extraMessage = LengthCodeBinaryType.getLengthCodeBinaryType(new byte[]{4, 3});

        ResultHeadPacket resultHeadPacket = new ResultHeadPacket(fieldNumber.getSize() + extraMessage.getSize());
        resultHeadPacket.setFieldNumber(fieldNumber);
        resultHeadPacket.setExtraMessage(extraMessage);

        /* 域描述报文 */
        LengthCodeStringType dataField = LengthCodeStringType.getLengthCodeString("def");
        LengthCodeStringType databaseName = LengthCodeStringType.getLengthCodeString("DATABASE");
        LengthCodeStringType tableAliasName = LengthCodeStringType.getLengthCodeString("TABLE ALIAS NAME");
        LengthCodeStringType tableName = LengthCodeStringType.getLengthCodeString("TABLE NAME");
        LengthCodeStringType fieldAliasName = LengthCodeStringType.getLengthCodeString("FIELD ALIAS NAME");
        LengthCodeStringType fieldName = LengthCodeStringType.getLengthCodeString("FIELD NAME");
        IntegerType fillNumber = IntegerType.getIntegerType(0xC0, FieldPacket.LENGTH_FILL_NUMBER);
        IntegerType characterSet = IntegerType.getIntegerType(0, FieldPacket.LENGTH_CHARACTER_SET);
        IntegerType filedLength = IntegerType.getIntegerType(1000000, FieldPacket.LENGTH_FIELD_LENGTH);
        IntegerType fieldTypeCode = IntegerType.getIntegerType(3, FieldPacket.LENGTH_FIELD_TYPE_CODE);
        IntegerType fieldFlagBitMask = IntegerType.getIntegerType(0x0002, FieldPacket.LENGTH_FIELD_FLAG_BIT_MASK);
        IntegerType decimalPointPrecision = IntegerType.getIntegerType(1, FieldPacket.LENGTH_DECIMAL_POINT_PRECISION);
        IntegerType reservedField = IntegerType.getIntegerType(0, FieldPacket.LENGTH_RESERVED_FIELD);
        LengthCodeStringType defaultValue = LengthCodeStringType.getLengthCodeString("DEFAULT VALUE");

        FieldPacket fieldPacket = new FieldPacket(dataField.getSize() + databaseName.getSize() + tableAliasName.getSize() + tableName.getSize() + fieldAliasName.getSize() + fieldName.getSize() + fillNumber.getSize() + characterSet.getSize() + filedLength.getSize() + fieldTypeCode.getSize() + fieldFlagBitMask.getSize() + decimalPointPrecision.getSize() + reservedField.getSize() + defaultValue.getSize());
        fieldPacket.setDataField(dataField);
        fieldPacket.setDatabaseName(databaseName);
        fieldPacket.setTableAliasName(tableAliasName);
        fieldPacket.setTableName(tableName);
        fieldPacket.setFieldAliasName(fieldAliasName);
        fieldPacket.setFieldName(fieldName);
        fieldPacket.setFillNumber(fillNumber);
        fieldPacket.setCharacterSet(characterSet);
        fieldPacket.setFieldLength(filedLength);
        fieldPacket.setFieldTypeCode(fieldTypeCode);
        fieldPacket.setFieldFlagBitMask(fieldFlagBitMask);
        fieldPacket.setDecimalPointPrecision(decimalPointPrecision);
        fieldPacket.setReservedField(reservedField);
        fieldPacket.setDefaultValue(defaultValue);

        FieldPacket[] fieldPacketArray = new FieldPacket[] {fieldPacket, fieldPacket};

        /* EOF报文1 */
        IntegerType packetIdentifier = IntegerType.getIntegerType(0xFE, EOFPacket.LENGTH_PACKET_IDENTIFIER);
        IntegerType warningNumber = IntegerType.getIntegerType(2, EOFPacket.LENGTH_WARNING_NUMBER);
        IntegerType serverStatusBitMask = IntegerType.getIntegerType(0xFFFF, EOFPacket.LENGTH_SERVER_STATUS_BIT_MASK);

        EOFPacket eofPacket = new EOFPacket(packetIdentifier.getSize() + warningNumber.getSize() + serverStatusBitMask.getSize());
        eofPacket.setPacketIdentifier(packetIdentifier);
        eofPacket.setWarningNumber(warningNumber);
        eofPacket.setServerStatusBitMask(serverStatusBitMask);

        /* 行数据报文 */
        LengthCodeStringType[] rowDataArray = new LengthCodeStringType[] {LengthCodeStringType.getLengthCodeString("111"), LengthCodeStringType.getLengthCodeString("222"), LengthCodeStringType.getLengthCodeString("333")};
        RowDataPacket rowDataPacket = new RowDataPacket(12);
        rowDataPacket.setRowData(rowDataArray);

        RowDataPacket[] rowDataPacketArray = new RowDataPacket[] {rowDataPacket, rowDataPacket, rowDataPacket};

        /* 构建结果集报文 */
        ResultSetPacket resultSetPacket = new ResultSetPacket(resultHeadPacket.getSize() + fieldPacket.getSize()*2 + eofPacket.getSize()*2 + rowDataPacket.getSize()*3);
        resultSetPacket.setResultHeadPacket(resultHeadPacket);
        resultSetPacket.setFieldPacketArray(fieldPacketArray);
        resultSetPacket.setEOFPacket1(eofPacket);
        resultSetPacket.setRowDataPacketArray(rowDataPacketArray);
        resultSetPacket.setEOFPacket2(eofPacket);

        System.out.println("resultSetPacket : " + resultSetPacket);
        System.out.println();


        /* get方法输出 */
        ResultHeadPacket resultHeadPacketGet = resultSetPacket.getResultHeadPacket();
        System.out.println("resultHeadPacket : " + resultHeadPacketGet);

        FieldPacket[] fieldPacketArrayGet = resultSetPacket.getFieldPacketArray();
        for(FieldPacket fieldPacketGet : fieldPacketArrayGet) {
            System.out.println("fieldPacket : " + fieldPacketGet);
        }

        EOFPacket eofPacket1Get = resultSetPacket.getEOFPacket1();
        System.out.println("EOFPacket1 : " + eofPacket1Get);

        RowDataPacket[] rowDataPacketArrayGet = resultSetPacket.getRowDataPacketArray();
        for(RowDataPacket rowDataPacketGet : rowDataPacketArrayGet) {
            System.out.println("rowDataPacket : " + rowDataPacketGet);
        }

        EOFPacket eofPacket2Get = resultSetPacket.getEOFPacket2();
        System.out.println("EOFPacket2 : " + eofPacket2Get);


        /* 解析内容 */
        System.out.println();
        System.out.println("resultHeadPacket : " + resultHeadPacketGet);

        System.out.println();
        for(FieldPacket fieldPacketGet : fieldPacketArrayGet) {
            System.out.println("fieldPacket : " + fieldPacketGet);
        }

        System.out.println();
        System.out.println("EOFPacket1 : " + eofPacket1Get);

        System.out.println();
        for(RowDataPacket rowDataPacketGet : rowDataPacketArrayGet) {
            System.out.println("rowDataPacket : " + rowDataPacketGet);
        }

        System.out.println();
        System.out.println("EOFPacket2 : " + eofPacket2Get);

    }
    /* Output:
    resultSetPacket : [2, 1, 2, 2, 4, 3, 3, 100, 101, 102, 8, 68, 65, 84, 65, 66, 65, 83, 69, 16, 84, 65, 66, 76, 69, 32, 65, 76, 73, 65, 83, 32, 78, 65, 77, 69, 10, 84, 65, 66, 76, 69, 32, 78, 65, 77, 69, 16, 70, 73, 69, 76, 68, 32, 65, 76, 73, 65, 83, 32, 78, 65, 77, 69, 10, 70, 73, 69, 76, 68, 32, 78, 65, 77, 69, -64, 0, 0, 0, 15, 66, 64, 3, 0, 2, 1, 0, 0, 13, 68, 69, 70, 65, 85, 76, 84, 32, 86, 65, 76, 85, 69, 3, 100, 101, 102, 8, 68, 65, 84, 65, 66, 65, 83, 69, 16, 84, 65, 66, 76, 69, 32, 65, 76, 73, 65, 83, 32, 78, 65, 77, 69, 10, 84, 65, 66, 76, 69, 32, 78, 65, 77, 69, 16, 70, 73, 69, 76, 68, 32, 65, 76, 73, 65, 83, 32, 78, 65, 77, 69, 10, 70, 73, 69, 76, 68, 32, 78, 65, 77, 69, -64, 0, 0, 0, 15, 66, 64, 3, 0, 2, 1, 0, 0, 13, 68, 69, 70, 65, 85, 76, 84, 32, 86, 65, 76, 85, 69, -2, 0, 2, -1, -1, 3, 49, 49, 49, 3, 50, 50, 50, 3, 51, 51, 51, 3, 49, 49, 49, 3, 50, 50, 50, 3, 51, 51, 51, 3, 49, 49, 49, 3, 50, 50, 50, 3, 51, 51, 51, -2, 0, 2, -1, -1]

    resultHeadPacket : [2, 1, 2, 2, 4, 3]
    fieldPacket : [3, 100, 101, 102, 8, 68, 65, 84, 65, 66, 65, 83, 69, 16, 84, 65, 66, 76, 69, 32, 65, 76, 73, 65, 83, 32, 78, 65, 77, 69, 10, 84, 65, 66, 76, 69, 32, 78, 65, 77, 69, 16, 70, 73, 69, 76, 68, 32, 65, 76, 73, 65, 83, 32, 78, 65, 77, 69, 10, 70, 73, 69, 76, 68, 32, 78, 65, 77, 69, -64, 0, 0, 0, 15, 66, 64, 3, 0, 2, 1, 0, 0, 13, 68, 69, 70, 65, 85, 76, 84, 32, 86, 65, 76, 85, 69]
    fieldPacket : [3, 100, 101, 102, 8, 68, 65, 84, 65, 66, 65, 83, 69, 16, 84, 65, 66, 76, 69, 32, 65, 76, 73, 65, 83, 32, 78, 65, 77, 69, 10, 84, 65, 66, 76, 69, 32, 78, 65, 77, 69, 16, 70, 73, 69, 76, 68, 32, 65, 76, 73, 65, 83, 32, 78, 65, 77, 69, 10, 70, 73, 69, 76, 68, 32, 78, 65, 77, 69, -64, 0, 0, 0, 15, 66, 64, 3, 0, 2, 1, 0, 0, 13, 68, 69, 70, 65, 85, 76, 84, 32, 86, 65, 76, 85, 69]
    EOFPacket1 : [-2, 0, 2, -1, -1]
    rowDataPacket : [3, 49, 49, 49, 3, 50, 50, 50, 3, 51, 51, 51]
    rowDataPacket : [3, 49, 49, 49, 3, 50, 50, 50, 3, 51, 51, 51]
    rowDataPacket : [3, 49, 49, 49, 3, 50, 50, 50, 3, 51, 51, 51]
    EOFPacket2 : [-2, 0, 2, -1, -1]
     */

}
