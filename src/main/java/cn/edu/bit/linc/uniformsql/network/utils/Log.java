package cn.edu.bit.linc.uniformsql.network.utils;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 日志输出类
 */
public class Log {
    private static final Logger LOG;

    static {
        // TODO: 设置 Log 输出文件
        LOG = Logger.getLogger("log.txt");
    }

    public static void info(String format, Object... vals) {
        String msg = MessageFormat.format(format, vals);
        LOG.info(msg);
    }

    public static void error(String format, Throwable err, Object... vals) {
        String msg = MessageFormat.format(format, vals);
        LOG.log(Level.SEVERE, msg, err);
    }
}
