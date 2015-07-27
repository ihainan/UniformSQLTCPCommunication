package cn.edu.bit.linc.uniformsql.network.packets;

import cn.edu.bit.linc.uniformsql.network.packets.type.IntegerType;
import cn.edu.bit.linc.uniformsql.network.packets.type.LengthCodeStringType;

/**
 * 域描述报文
 */
public class FieldPacket extends BasePacket{

    /**
     * 数据域的偏移量
     */
    public final static int OFFSET_DATA_FIELD = 0;

    /**
     * 数据域的长度
     */
    private int dataLen = -1;

    /**
     * 数据库名称的偏移量 - 数据域长度
     */
    public final static int OFFSET_DATABASE_NAME_MINUS_DATA_LENGTH = 0;

    /**
     * 数据库名称的长度
     */
    private int databaseLen = -1;

    /**
     * 数据表别名的偏移量 - 数据库名称的长度
     */
    public final static int OFFSET_TABLE_ALIAS_NAME_NIMUS_DATABASE_LENGTH = 0;

    /**
     * 数据表名别的长度
     */
    private int tableAliasLen = -1;

    /**
     * 数据表名的偏移量 - 数据表别名的长度
     */
    public final static int OFFSET_TABLE_NAME_NIMUS_TABLE_ALIAS_LENGTH = 0;

    /**
     * 数据表名的长度
     */
    private int tableLen = -1;

    /**
     * 字段别名的偏移量 - 数据表名的长度
     */
    public final static int OFFSET_FIELD_ALIAS_NAME_NIMUS_TABLE_LENGTH = 0;

    /**
     * 字段别名的长度
     */
    private int fieldAliasLen = -1;

    /**
     * 字段名的偏移量 - 字段别名的长度
     */
    public final static int OFFSET_FIELD_NAME_NIMUS_FIELD_ALIAS_LENGTH = 0;

    /**
     * 字段名的长度
     */
    private int fieldLen = -1;

    /**
     * 填充值的偏移量 - 字段名的长度
     */
    public final static int OFFSET_FILL_NUMBER_MINUS_FIELD_LENGTH = 0;

    /**
     * 填充值的长度
     */
    public final static int LENGTH_FILL_NUMBER = 1;

    /**
     * 字符编码的偏移量 - 字段名的长度
     */
    public final static int OFFSET_CHARACTER_SET_MINUS_FIELD_LENGTH = 1;

    /**
     * 字符编码的长度
     */
    public final static int LENGTH_CHARACTER_SET = 2;

    /**
     * 字段长度的偏移量 - 字段名的长度
     */
    public final static int OFFSET_FIELD_LENGTH_MINUS_FIELD_LENGTH = 3;

    /**
     * 字段长度的长度
     */
    public final static int LENGTH_FIELD_LENGTH = 4;

    /**
     * 字段类型编码的偏移量 - 字段名的长度
     */
    public final static int OFFSET_FIELD_TYPE_CODE_MINUS_FIELD_LENGTH = 7;

    /**
     * 字段类型编码的长度
     */
    public final static int LENGTH_FIELD_TYPE_CODE = 1;

    /**
     * 字段标志掩码的偏移量 - 字段名的长度
     */
    public final static int OFFSET_FIELD_FLAG_BIT_MASK_MINUS_FIELD_LENGTH = 8;

    /**
     * 字段标志掩码的长度
     */
    public final static int LENGTH_FIELD_FLAG_BIT_MASK = 2;

    /**
     * 整型值精度的偏移量 - 字段名的长度
     */
    public final static int OFFSET_DECIMAL_POINT_PRECISION_MINUS_FIELD_LENGTH = 10;

    /**
     * 整型值精度的长度
     */
    public final static int LENGTH_DECIMAL_POINT_PRECISION = 1;

    /**
     * 保留字段的偏移量 - 字段名的长度
     */
    public final static int OFFSET_RESERVED_FIELD_MINUS_FIELD_LENGTH = 11;

    /**
     * 保留字段的长度
     */
    public final static int LENGTH_RESERVED_FIELD = 2;

    /**
     * 默认值的偏移量 - 字段名的长度
     */
    public final static int OFFSET_DEFAULT_VALUE_MINUS_FIELD_LENGTH = 13;

    /**
     * 默认值的长度
     */
    private int defaultValueLen = -1;

    /**
     * 创建指定大小的域描述报文
     *
     * @param size 指定大小（字节为单位）
     */
    public FieldPacket(int size) {
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
     * 设置数据域字段
     *
     * @param dataField 数据域
     */
    public void setDataField(LengthCodeStringType dataField) {
        byte[] bytes = new byte[dataField.getSize()];
        dataLen = dataField.getSize();
        dataField.getData(bytes);
        System.arraycopy(bytes, 0, _data_, OFFSET_DATA_FIELD, bytes.length);
    }

    /**
     * 获取数据域字段
     *
     * @return 数据域
     */
    public LengthCodeStringType getDataField() {
        byte[] tmp = new byte[_data_.length - OFFSET_DATA_FIELD];
        System.arraycopy(_data_, OFFSET_DATA_FIELD, tmp, 0, tmp.length);
        dataLen = LengthCodeStringType.getLength(tmp);

        byte[] data = new byte[dataLen];
        System.arraycopy(_data_, OFFSET_DATA_FIELD, data, 0, data.length);
        return LengthCodeStringType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 设置数据库名称字段
     *
     * @param databaseName 数据库名称
     */
    public void setDatabaseName(LengthCodeStringType databaseName) {
        byte[] bytes = new byte[databaseName.getSize()];
        databaseLen = databaseName.getSize();
        databaseName.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + OFFSET_DATABASE_NAME_MINUS_DATA_LENGTH, bytes.length);
    }

    /**
     * 获取数据库名称字段
     *
     * @return 数据库名称
     */
    public LengthCodeStringType getDatabaseName() {
        byte[] tmp = new byte[_data_.length - dataLen - OFFSET_DATABASE_NAME_MINUS_DATA_LENGTH];
        System.arraycopy(_data_, dataLen + OFFSET_DATABASE_NAME_MINUS_DATA_LENGTH, tmp, 0, tmp.length);
        databaseLen = LengthCodeStringType.getLength(tmp);

        byte[] data = new byte[databaseLen];
        System.arraycopy(_data_, dataLen + OFFSET_DATABASE_NAME_MINUS_DATA_LENGTH, data, 0, data.length);
        return LengthCodeStringType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 设置数据表别名字段
     *
     * @param tableAliasName 数据表别名
     */
    public void setTableAliasName(LengthCodeStringType tableAliasName) {
        byte[] bytes = new byte[tableAliasName.getSize()];
        tableAliasLen = tableAliasName.getSize();
        tableAliasName.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + databaseLen + OFFSET_TABLE_ALIAS_NAME_NIMUS_DATABASE_LENGTH, bytes.length);
    }

    /**
     * 获取数据表别名字段
     *
     * @return 数据表别名
     */
    public LengthCodeStringType getTableAliasName() {
        byte[] tmp = new byte[_data_.length - dataLen - databaseLen - OFFSET_TABLE_ALIAS_NAME_NIMUS_DATABASE_LENGTH];
        System.arraycopy(_data_, dataLen + databaseLen + OFFSET_TABLE_ALIAS_NAME_NIMUS_DATABASE_LENGTH, tmp, 0, tmp.length);
        tableAliasLen = LengthCodeStringType.getLength(tmp);

        byte[] data = new byte[tableAliasLen];
        System.arraycopy(_data_, dataLen + databaseLen + OFFSET_TABLE_ALIAS_NAME_NIMUS_DATABASE_LENGTH, data, 0, data.length);
        return LengthCodeStringType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 设置数据表名字段
     *
     * @param tableName 数据表名
     */
    public void setTableName(LengthCodeStringType tableName) {
        byte[] bytes = new byte[tableName.getSize()];
        tableLen = tableName.getSize();
        tableName.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + databaseLen + tableAliasLen + OFFSET_TABLE_NAME_NIMUS_TABLE_ALIAS_LENGTH, bytes.length);
    }

    /**
     * 获取数据表名字段
     *
     * @return 数据表名
     */
    public LengthCodeStringType getTableName() {
        byte[] tmp = new byte[_data_.length - dataLen - databaseLen - tableAliasLen - OFFSET_TABLE_NAME_NIMUS_TABLE_ALIAS_LENGTH];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + OFFSET_TABLE_NAME_NIMUS_TABLE_ALIAS_LENGTH, tmp, 0, tmp.length);
        tableLen = LengthCodeStringType.getLength(tmp);

        byte[] data = new byte[tableLen];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + OFFSET_TABLE_NAME_NIMUS_TABLE_ALIAS_LENGTH, data, 0, data.length);
        return LengthCodeStringType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 设置字段别名字段
     *
     * @param fieldAliasName 字段别名
     */
    public void setFieldAliasName(LengthCodeStringType fieldAliasName) {
        byte[] bytes = new byte[fieldAliasName.getSize()];
        fieldAliasLen = fieldAliasName.getSize();
        fieldAliasName.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + databaseLen + tableAliasLen + tableLen + OFFSET_FIELD_ALIAS_NAME_NIMUS_TABLE_LENGTH, bytes.length);
    }

    /**
     * 获取字段别名字段
     *
     * @return 字段别名
     */
    public LengthCodeStringType getFieldAliasName() {
        byte[] tmp = new byte[_data_.length - dataLen - databaseLen - tableAliasLen - tableLen - OFFSET_FIELD_ALIAS_NAME_NIMUS_TABLE_LENGTH];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + OFFSET_FIELD_ALIAS_NAME_NIMUS_TABLE_LENGTH, tmp, 0, tmp.length);
        fieldAliasLen = LengthCodeStringType.getLength(tmp);

        byte[] data = new byte[fieldAliasLen];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + OFFSET_FIELD_ALIAS_NAME_NIMUS_TABLE_LENGTH, data, 0, data.length);
        return LengthCodeStringType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 设置字段名字段
     *
     * @param fieldName 字段名
     */
    public void setFieldName(LengthCodeStringType fieldName) {
        byte[] bytes = new byte[fieldName.getSize()];
        fieldLen = fieldName.getSize();
        fieldName.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + OFFSET_FIELD_NAME_NIMUS_FIELD_ALIAS_LENGTH, bytes.length);
    }

    /**
     * 获取字段名字段
     *
     * @return 字段名
     */
    public LengthCodeStringType getFieldName() {
        byte[] tmp = new byte[_data_.length - dataLen - databaseLen - tableAliasLen - tableLen - fieldAliasLen - OFFSET_FIELD_NAME_NIMUS_FIELD_ALIAS_LENGTH];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + OFFSET_FIELD_NAME_NIMUS_FIELD_ALIAS_LENGTH, tmp, 0, tmp.length);
        fieldLen = LengthCodeStringType.getLength(tmp);

        byte[] data = new byte[fieldLen];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + OFFSET_FIELD_NAME_NIMUS_FIELD_ALIAS_LENGTH, data, 0, data.length);
        return LengthCodeStringType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 设置填充值字段
     *
     * @param fillNumber 填充值
     */
    public void setFillNumber(IntegerType fillNumber) {
        byte[] bytes = new byte[fillNumber.getSize()];
        fillNumber.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_FILL_NUMBER_MINUS_FIELD_LENGTH, bytes.length);
    }

    /**
     * 获取填充值字段
     *
     * @return 填充值
     */
    public IntegerType getFillNumber() {
        byte[] data = new byte[LENGTH_FILL_NUMBER];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_FILL_NUMBER_MINUS_FIELD_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置字符编码字段
     *
     * @param characterSet 字符编码
     */
    public void setCharacterSet(IntegerType characterSet) {
        byte[] bytes = new byte[characterSet.getSize()];
        characterSet.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_CHARACTER_SET_MINUS_FIELD_LENGTH, bytes.length);
    }

    /**
     * 获取字符编码字段
     *
     * @return 字符编码
     */
    public IntegerType getCharacterSet() {
        byte[] data = new byte[LENGTH_CHARACTER_SET];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_CHARACTER_SET_MINUS_FIELD_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置字段长度字段
     *
     * @param fieldLength 字段长度
     */
    public void setFieldLength(IntegerType fieldLength) {
        byte[] bytes = new byte[fieldLength.getSize()];
        fieldLength.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_FIELD_LENGTH_MINUS_FIELD_LENGTH, bytes.length);
    }

    /**
     * 获取字段长度字段
     *
     * @return 字段长度
     */
    public IntegerType getFieldLength() {
        byte[] data = new byte[LENGTH_FIELD_LENGTH];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_FIELD_LENGTH_MINUS_FIELD_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置字段类型编码字段
     *
     * @param fieldTypeCode 字段类型编码
     */
    public void setFieldTypeCode(IntegerType fieldTypeCode) {
        byte[] bytes = new byte[fieldTypeCode.getSize()];
        fieldTypeCode.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_FIELD_TYPE_CODE_MINUS_FIELD_LENGTH, bytes.length);
    }

    /**
     * 获取字段类型编码字段
     *
     * @return 字段类型编码
     */
    public IntegerType getFieldTypeCode() {
        byte[] data = new byte[LENGTH_FIELD_TYPE_CODE];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_FIELD_TYPE_CODE_MINUS_FIELD_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置字段标志掩码字段
     *
     * @param fieldFlagBitMask 字段标志掩码
     */
    public void setFieldFlagBitMask(IntegerType fieldFlagBitMask) {
        byte[] bytes = new byte[fieldFlagBitMask.getSize()];
        fieldFlagBitMask.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_FIELD_FLAG_BIT_MASK_MINUS_FIELD_LENGTH, bytes.length);
    }

    /**
     * 获取字段标志掩码字段
     *
     * @return 字段标志掩码
     */
    public IntegerType getFieldFlagBitMask() {
        byte[] data = new byte[LENGTH_FIELD_FLAG_BIT_MASK];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_FIELD_FLAG_BIT_MASK_MINUS_FIELD_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置整型值精度字段
     *
     * @param decimalPointPrecision 整型值精度
     */
    public void setDecimalPointPrecision(IntegerType decimalPointPrecision) {
        byte[] bytes = new byte[decimalPointPrecision.getSize()];
        decimalPointPrecision.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_DECIMAL_POINT_PRECISION_MINUS_FIELD_LENGTH, bytes.length);
    }

    /**
     * 获取整型值精度字段
     *
     * @return 整型值精度
     */
    public IntegerType getDecimalPointPrecision() {
        byte[] data = new byte[LENGTH_DECIMAL_POINT_PRECISION];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_DECIMAL_POINT_PRECISION_MINUS_FIELD_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置保留字段
     *
     * @param reservedField 保留字段
     */
    public void setReservedField(IntegerType reservedField) {
        byte[] bytes = new byte[reservedField.getSize()];
        reservedField.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_RESERVED_FIELD_MINUS_FIELD_LENGTH, bytes.length);
    }

    /**
     * 获取保留字段
     *
     * @return 保留字段
     */
    public IntegerType getReservedField() {
        byte[] data = new byte[LENGTH_RESERVED_FIELD];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_RESERVED_FIELD_MINUS_FIELD_LENGTH, data, 0, data.length);
        return IntegerType.getIntegerType(data);
    }

    /**
     * 设置默认值字段
     *
     * @param defaultValue 默认值
     */
    public void setDefaultValue(LengthCodeStringType defaultValue) {
        byte[] bytes = new byte[defaultValue.getSize()];
        defaultValueLen = defaultValue.getSize();
        defaultValue.getData(bytes);
        System.arraycopy(bytes, 0, _data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_DEFAULT_VALUE_MINUS_FIELD_LENGTH, bytes.length);
    }

    /**
     * 获取默认值字段
     *
     * @return 默认值
     */
    public LengthCodeStringType getDefaultValue() {
        byte[] tmp = new byte[_data_.length - dataLen - databaseLen - tableAliasLen - tableLen - fieldAliasLen - fieldLen - OFFSET_DEFAULT_VALUE_MINUS_FIELD_LENGTH];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_DEFAULT_VALUE_MINUS_FIELD_LENGTH, tmp, 0, tmp.length);
        defaultValueLen = LengthCodeStringType.getLength(tmp);

        byte[] data = new byte[defaultValueLen];
        System.arraycopy(_data_, dataLen + databaseLen + tableAliasLen + tableLen + fieldAliasLen + fieldLen + OFFSET_DEFAULT_VALUE_MINUS_FIELD_LENGTH, data, 0, data.length);
        return LengthCodeStringType.getLengthCodeBinaryTypeUsingData(data);
    }

    /**
     * 测试函数
     *
     * @param args 程序参数
     */
    public static void main(String[] args) throws PacketExceptions.NecessaryFieldNotSetException {

        LengthCodeStringType dataField = LengthCodeStringType.getLengthCodeString("def");
        LengthCodeStringType databaseName = LengthCodeStringType.getLengthCodeString("DATABASE");
        LengthCodeStringType tableAliasName = LengthCodeStringType.getLengthCodeString("TABLE ALIAS NAME");
        LengthCodeStringType tableName = LengthCodeStringType.getLengthCodeString("TABLE NAME");
        LengthCodeStringType fieldAliasName = LengthCodeStringType.getLengthCodeString("FIELD ALIAS NAME");
        LengthCodeStringType fieldName = LengthCodeStringType.getLengthCodeString("FIELD NAME");
        IntegerType fillNumber = IntegerType.getIntegerType(0xC0, LENGTH_FILL_NUMBER);
        IntegerType characterSet = IntegerType.getIntegerType(0, LENGTH_CHARACTER_SET);
        IntegerType filedLength = IntegerType.getIntegerType(1000000, LENGTH_FIELD_LENGTH);
        IntegerType fieldTypeCode = IntegerType.getIntegerType(3, LENGTH_FIELD_TYPE_CODE);
        IntegerType fieldFlagBitMask = IntegerType.getIntegerType(0x0002, LENGTH_FIELD_FLAG_BIT_MASK);
        IntegerType decimalPointPrecision = IntegerType.getIntegerType(1, LENGTH_DECIMAL_POINT_PRECISION);
        IntegerType reservedField = IntegerType.getIntegerType(0, LENGTH_RESERVED_FIELD);
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

        System.out.println(fieldPacket);
        System.out.println("Data Field          : " + LengthCodeStringType.getString(fieldPacket.getDataField()));
        System.out.println("Database Name       : " + LengthCodeStringType.getString(fieldPacket.getDatabaseName()));
        System.out.println("Table Alias Name    : " + LengthCodeStringType.getString(fieldPacket.getTableAliasName()));
        System.out.println("Table Name          : " + LengthCodeStringType.getString(fieldPacket.getTableName()));
        System.out.println("Field Alias Name    : " + LengthCodeStringType.getString(fieldPacket.getFieldAliasName()));
        System.out.println("Field Name          : " + LengthCodeStringType.getString(fieldPacket.getFieldName()));
        System.out.println("Fill Number         : " + IntegerType.getIntegerValue(fieldPacket.getFillNumber()));
        System.out.println("Character Set       : " + IntegerType.getIntegerValue(fieldPacket.getCharacterSet()));
        System.out.println("Field Length        : " + IntegerType.getIntegerValue(fieldPacket.getFieldLength()));
        System.out.println("Field Type Code     : " + IntegerType.getIntegerValue(fieldPacket.getFieldTypeCode()));
        System.out.println("Field Flag Bit Mask : " + IntegerType.getIntegerValue(fieldPacket.getFieldFlagBitMask()));
        System.out.println("Decimal Point Pre   : " + IntegerType.getIntegerValue(fieldPacket.getDecimalPointPrecision()));
        System.out.println("Reserved Field      : " + IntegerType.getIntegerValue(fieldPacket.getReservedField()));
        System.out.println("Default Value       : " + LengthCodeStringType.getString(fieldPacket.getDefaultValue()));

         /* 通过byte[]构建 */
        FieldPacket fieldPacketCopy = new FieldPacket(fieldPacket.getSize());
        byte[] data = new byte[fieldPacket.getSize()];
        fieldPacket.getData(data);
        fieldPacketCopy.setData(data);
        System.out.println();

        System.out.println(fieldPacketCopy);
        System.out.println("Data Field          : " + LengthCodeStringType.getString(fieldPacketCopy.getDataField()));
        System.out.println("Database Name       : " + LengthCodeStringType.getString(fieldPacketCopy.getDatabaseName()));
        System.out.println("Table Alias Name    : " + LengthCodeStringType.getString(fieldPacketCopy.getTableAliasName()));
        System.out.println("Table Name          : " + LengthCodeStringType.getString(fieldPacketCopy.getTableName()));
        System.out.println("Field Alias Name    : " + LengthCodeStringType.getString(fieldPacketCopy.getFieldAliasName()));
        System.out.println("Field Name          : " + LengthCodeStringType.getString(fieldPacketCopy.getFieldName()));
        System.out.println("Fill Number         : " + IntegerType.getIntegerValue(fieldPacketCopy.getFillNumber()));
        System.out.println("Character Set       : " + IntegerType.getIntegerValue(fieldPacketCopy.getCharacterSet()));
        System.out.println("Field Length        : " + IntegerType.getIntegerValue(fieldPacketCopy.getFieldLength()));
        System.out.println("Field Type Code     : " + IntegerType.getIntegerValue(fieldPacketCopy.getFieldTypeCode()));
        System.out.println("Field Flag Bit Mask : " + IntegerType.getIntegerValue(fieldPacketCopy.getFieldFlagBitMask()));
        System.out.println("Decimal Point Pre   : " + IntegerType.getIntegerValue(fieldPacketCopy.getDecimalPointPrecision()));
        System.out.println("Reserved Field      : " + IntegerType.getIntegerValue(fieldPacketCopy.getReservedField()));
        System.out.println("Default Value       : " + LengthCodeStringType.getString(fieldPacketCopy.getDefaultValue()));

    }
    /* Output:
    [3, 100, 101, 102, 8, 68, 65, 84, 65, 66, 65, 83, 69, 16, 84, 65, 66, 76, 69, 32, 65, 76, 73, 65, 83, 32, 78, 65, 77, 69, 10, 84, 65, 66, 76, 69, 32, 78, 65, 77, 69, 16, 70, 73, 69, 76, 68, 32, 65, 76, 73, 65, 83, 32, 78, 65, 77, 69, 10, 70, 73, 69, 76, 68, 32, 78, 65, 77, 69, -64, 0, 0, 0, 15, 66, 64, 3, 0, 2, 1, 0, 0, 13, 68, 69, 70, 65, 85, 76, 84, 32, 86, 65, 76, 85, 69]
    Data Field          : def
    Database Name       : DATABASE
    Table Alias Name    : TABLE ALIAS NAME
    Table Name          : TABLE NAME
    Field Alias Name    : FIELD ALIAS NAME
    Field Name          : FIELD NAME
    Fill Number         : 192
    Character Set       : 0
    Field Length        : 1000000
    Field Type Code     : 3
    Field Flag Bit Mask : 2
    Decimal Point Pre   : 1
    Reserved Field      : 0
    Default Value       : DEFAULT VALUE

    [3, 100, 101, 102, 8, 68, 65, 84, 65, 66, 65, 83, 69, 16, 84, 65, 66, 76, 69, 32, 65, 76, 73, 65, 83, 32, 78, 65, 77, 69, 10, 84, 65, 66, 76, 69, 32, 78, 65, 77, 69, 16, 70, 73, 69, 76, 68, 32, 65, 76, 73, 65, 83, 32, 78, 65, 77, 69, 10, 70, 73, 69, 76, 68, 32, 78, 65, 77, 69, -64, 0, 0, 0, 15, 66, 64, 3, 0, 2, 1, 0, 0, 13, 68, 69, 70, 65, 85, 76, 84, 32, 86, 65, 76, 85, 69]
    Data Field          : def
    Database Name       : DATABASE
    Table Alias Name    : TABLE ALIAS NAME
    Table Name          : TABLE NAME
    Field Alias Name    : FIELD ALIAS NAME
    Field Name          : FIELD NAME
    Fill Number         : 192
    Character Set       : 0
    Field Length        : 1000000
    Field Type Code     : 3
    Field Flag Bit Mask : 2
    Decimal Point Pre   : 1
    Reserved Field      : 0
    Default Value       : DEFAULT VALUE
     */

}
