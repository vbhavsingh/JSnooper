package net.rationalminds.transaction.guid;

import java.util.UUID;

public class UID {

	private static UID gen = null;
	private static UUID value;

	private UID() {
	}

	public static String get() {
		if (gen == null) {
			gen = new UID();
		}
		value = UUID.randomUUID();
		return value.toString();
	}

}
