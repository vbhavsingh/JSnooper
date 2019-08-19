package net.rationalminds.util.LogManager;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class JSnooperLogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		StringBuilder builder = new StringBuilder(1000);
		builder.append("JSnooper: ");
        builder.append("[").append(record.getSourceClassName()).append("] - ");
        builder.append("[").append(record.getLevel()).append("] - ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
	}

}
