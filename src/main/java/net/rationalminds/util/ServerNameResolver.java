package net.rationalminds.util;

public class ServerNameResolver {
	
	public static String resolve(String serverType){
		if (serverType.equals(CONSTANTS.SERVER.WLS)){
			return System.getProperty("weblogic.Name");
		}
		if (serverType.equals(CONSTANTS.SERVER.WS)){
			return System.getProperty("J2EEServer");
		}
		return "NA";
	}

}
