package net.rationalminds.util.LogManager;

public interface JSnooperLogger {
	public void error(Object message);

	public void error(Object message, Throwable t);

	public void warn(Object message);
	
	public void warn(Object message, Throwable t);

	public void info(Object message);
	
	public void info(Object message, Throwable t);

	public void debug(Object message);
	
	public void debug(Object message, Throwable t);

}
