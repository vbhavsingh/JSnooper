package net.rationalminds.transfer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.rationalminds.apache.http.client.HttpClient;
import net.rationalminds.apache.http.impl.client.DefaultHttpClient;
import net.rationalminds.util.PropertyManager;
import net.rationalminds.util.URL;

public class ServiceLocator {

	private static Map map;
	private static short roundRobin = 0;
	private static HttpClient client;

	/**
	 * create one time array of load balanced url's
	 */
	private ServiceLocator() {
		map = new HashMap();
	    client = new DefaultHttpClient();

		String uri[] = PropertyManager.getVal("WSDL_URL").split(",");
		for (int i = 0; i < uri.length; i++) {
			URL location = new URL();
			location.setUri(uri[i]);
			location.setActive(true);
			location.setInactiveSince(new Date());
			map.put(i, location);
		}
	}

	public static synchronized String getLocation() {
		if (roundRobin == map.size()) {
			roundRobin = 0;
		}

		return null;
	}

}
