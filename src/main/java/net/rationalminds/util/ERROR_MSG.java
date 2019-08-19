package net.rationalminds.util;

public class ERROR_MSG {

	public static final class IBM {
		public static final String JNDI_FAIL = "J-SNOOPER-WS001: JNDI lookup failed";

		public static final String PARTITION_EXIST = "J-SNOOPER-WS002: Websphere work partition already exists";

		public static final String ILLEGAL_ACCESS = "J-SNOOPER-WS003: Not able to create partition";

		public static final String PARTITION_NOT_EXIST = "J-SNOOPER-WS003: Websphere work partition not exist";
	}

	public static final class HTTP {
		public static final String NULL_CONNECTION_W = "J-SNOOPER-.HTTP.001: Writing property to connection header failed, connection object is null";
		
		public static final String NULL_CONNECTION_R = "J-SNOOPER-.HTTP.002: Reading property to connection header failed, connection object is null";

	}

}
