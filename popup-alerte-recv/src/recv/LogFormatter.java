package recv;

import java.time.LocalDateTime;
import java.util.logging.*;

public class LogFormatter extends Formatter
{
	public String format(LogRecord record)
	{
		LocalDateTime dt = LocalDateTime.now();
		String fdt = dt.toString().replace('T', ' ');
		String ret = fdt + " [" + record.getLevel() + "] in " +
		record.getSourceClassName() + " " + record.getSourceMethodName() +
		": " + record.getMessage() + "\n";
		
		return ret;
	}
}
