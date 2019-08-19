package net.rationalminds.util;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class PropertyManager {

	private static Properties props;
	public static String SERVER_TYPE;
	public static String LOG_MODE;
	public static String LOG_APPENDER;
	public static String HOST_ID;
	public static String TRANSPORT_MODE;
	public static String HTTP_CONNECTION_COUNT;
	public static List<String> CORRELATION_MODES;
	public static int POOL_SIZE;
	public static Properties configProps;
	public static String CONFIG_FILE_PATH;

	static {
		props = new Properties();
		boolean propertyFileDefined = checkDefinedPropertyFile();
		if (propertyFileDefined) {

		} else {
			try {
				/**
				 * First check for property in classpath of the application. If
				 * property file is in classpath of load it as resource.
				 */
				props.load(ClassLoader
						.getSystemResourceAsStream(CONSTANTS.PROP_FILE));
			} catch (Exception ex) {
				System.out.println("PROPERTIES FILE NOT FOUND IN CLASSPATH");

				/**
				 * If property path is not in visible area of current
				 * classloader, get all the files that are in classpath by url.
				 * Check for file that bears same name as property file. Read
				 * the file using file input stream from physical location.
				 */
				ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
				// Get the URLs
				URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
				String path = null;
				for (int i = 0; i < urls.length; i++) {
					urls[i].getFile();
					if (urls[i].getFile().contains(CONSTANTS.PROP_FILE)) {
						path = urls[i].getFile();
						break;
					}
				}
				try {
					props.load(new FileInputStream(path));
				} catch (Exception e) {

					/**
					 * if property file is no where to be found. Agent data
					 * collection would not be started and put into freeze
					 * status.
					 */
					System.out
							.println("PROPERTIES FILE NOT FOUND, AGENT HALTED");
					CONSTANTS.CONTROL.PROBE_ON = false;
				}
			}
		}

		/**
		 * Read the configurations as property file entity. Check for property
		 * file override as "-Dx.y.z=val" system property else put default
		 * attributes.
		 */
		LOG_APPENDER = serachParam(CONSTANTS.LOG.LOG_APPENDER);
		if (LOG_APPENDER == null || LOG_APPENDER.equals("")) {
			LOG_APPENDER = CONSTANTS.LOG.LOG_APPENDER_DEFAULT;
		}
		LOG_APPENDER = LOG_APPENDER.toLowerCase();

		SERVER_TYPE = serachParam(CONSTANTS.SERVER.SERVER_TYPE);
		if (SERVER_TYPE == null || SERVER_TYPE.equals("")) {
			SERVER_TYPE = CONSTANTS.SERVER.DEFAULT;
		}
		SERVER_TYPE = SERVER_TYPE.toLowerCase();

		LOG_MODE = serachParam(CONSTANTS.LOG.LOG_LEVEL);
		if (LOG_MODE == null || LOG_MODE.equals("")) {
			LOG_MODE = CONSTANTS.LOG.LOG_LEVEL_NOLOG;
		}
		LOG_MODE = LOG_MODE.toLowerCase();

		TRANSPORT_MODE = serachParam(CONSTANTS.TRANSPORT_MODE);
		if (TRANSPORT_MODE == null || TRANSPORT_MODE.equals("")) {
			TRANSPORT_MODE = CONSTANTS.TRANSPORT_SERVLET;
		}
		TRANSPORT_MODE = TRANSPORT_MODE.toLowerCase();

		/* Added to identify how GUID can be transferred */
		//String TECH_MODES = serachParam(CONSTANTS.TECH.TECH);
		String TECH_MODES = CONSTANTS.TECH.APACHE+":"+CONSTANTS.TECH.CXF_WS+":"+CONSTANTS.TECH.HTTP+":"+CONSTANTS.TECH.JAX_RPC+":"+CONSTANTS.TECH.JAX_WS_JAVA6+":"+CONSTANTS.TECH.WEB_SERVICE_HTTP +":"+CONSTANTS.TECH.JMS;
		CORRELATION_MODES = new ArrayList<String>();
		if (TECH_MODES == null || TECH_MODES.equals("")) {
			CORRELATION_MODES.add(CONSTANTS.TECH.UNKNOWN);
			System.out.println("Propgation modes not found");
		} else {
			String modes[] = TECH_MODES.split(":");
			for (int i = 0; modes != null && i < modes.length; i++) {
				CORRELATION_MODES.add(modes[i]);
				//System.out.println("Propgation modes:" + modes[i]);
			}
		}

		HOST_ID = getSystemName();

		Integer poolSize = Integer.getInteger(CONSTANTS.DATA_POOL_SIZE);
		if (poolSize == null) {
			POOL_SIZE = 2500;
		} else {
			POOL_SIZE = poolSize;
		}

		// now read the config version
		configProps = new Properties();

		try {
			configProps.load(ClassLoader
					.getSystemResourceAsStream(CONSTANTS.CONFIG_FILE));
		} catch (Exception ex) {
			System.out.println("CONFIG PROPERTIES FILE NOT FOUND IN CLASSPATH");
			ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();
			// Get the URLs
			URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();
			String path = null;
			for (int i = 0; i < urls.length; i++) {
				urls[i].getFile();
				if (urls[i].getFile().contains(CONSTANTS.CONFIG_FILE)) {
					path = urls[i].getFile();
					break;
				}
			}
			try {
				configProps.load(new FileInputStream(path));
				CONFIG_FILE_PATH = path;
			} catch (Exception e) {
				System.out.println("CONFIG PROPERTIES FILE NOT FOUND");
			}
		}

	}

	public static String getVal(String key) {
		return props.getProperty(key);
	}

	public static Integer getInteger(String key) {
		try {
			return Integer.parseInt(props.getProperty(key));
		} catch (Exception e) {
			return null;
		}
	}

	public static Enumeration getKeys() {
		return props.keys();
	}

	/**
	 * This computed the address of the local environment. complete location has
	 * system name, tier name and server name.
	 * 
	 * @return complete_host_name: String
	 */
	private static String getSystemName() {
		String hostname;
		String domain;
		String server;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
			// LOGGER.LOG("hostname=" + hostname);
		} catch (UnknownHostException e) {
			hostname = "unknown";
		}
		domain = System.getenv("LOGNAME");
		server = ServerNameResolver.resolve(SERVER_TYPE);
		if (server == null) {
			server = "";
		}
		return hostname + "/" + domain + "/" + server;
	}

	/**
	 * Read the configurations as property file entity. Check for property file
	 * override as "-Dx.y.z=val" system property.
	 */
	public static String serachParam(String key) {
		if (System.getProperty(key) == null) {
			return PropertyManager.getVal(key);
		} else {
			return System.getProperty(key);

		}

	}

	private static boolean checkDefinedPropertyFile() {
		if (System.getProperty(CONSTANTS.JSNOOPER) == null) {
			return false;
		} else {
			return true;

		}
	}

}
