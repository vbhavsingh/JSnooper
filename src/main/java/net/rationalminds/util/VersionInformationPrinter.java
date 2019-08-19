package net.rationalminds.util;


public class VersionInformationPrinter {

	private static boolean status = true;

	public synchronized static void print() {
		if (status) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(System.getProperty("line.separator"));
			buffer.append("*****************************************************************");
			buffer.append(System.getProperty("line.separator"));
			buffer.append("JSNOOPER AGENT VERSION. ");
			buffer.append(CONSTANTS.JSNOOPER_VERSION);
			buffer.append(" ENABLED");
			buffer.append(System.getProperty("line.separator"));
			buffer.append("*****************************************************************");
			System.out.println(buffer.toString());
			
			//ControllerClient cc = new ControllerClient();
			//cc.enableListner();
			status = false;
		}
	}
	
	public static boolean getStatus(){
		return status;
	}
}
