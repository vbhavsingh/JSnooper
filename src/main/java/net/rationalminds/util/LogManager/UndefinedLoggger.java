package net.rationalminds.util.LogManager;

public class UndefinedLoggger extends Exception {

	public UndefinedLoggger() {
		super("unknown error");
	}

	public UndefinedLoggger(String message) {
		super(message);
	}

	public UndefinedLoggger(Throwable t) {
		super(t);
	}

	public UndefinedLoggger(Exception e) {
		super(e);
	}

}
