package net.rationalminds.transaction.guid;

import net.rationalminds.transaction.ContextManager;
import net.rationalminds.transaction.JSnooperThreadLocal;
import net.rationalminds.transaction.WebSphereServiceContext;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;

public class GUIDSelectionFactory {

	private static GUIDPropgation guid = null;

	public static GUIDPropgation selectMedia() {
		if (PropertyManager.SERVER_TYPE.equals(CONSTANTS.SERVER.WLS)) {
			guid = new ContextManager();
		}
		if (PropertyManager.SERVER_TYPE.equals(CONSTANTS.SERVER.WS)) {
			guid = new WebSphereServiceContext();

		}
		if (PropertyManager.SERVER_TYPE.equals(CONSTANTS.SERVER.NO_SERVER)) {
			guid = new JSnooperThreadLocal();
		}
		return guid;
	}
	
	public static GUIDPropgation selectMedia(String uid) {

		if (PropertyManager.SERVER_TYPE.equals(CONSTANTS.SERVER.WLS)) {
			guid = new ContextManager();
		}
		if (PropertyManager.SERVER_TYPE.equals(CONSTANTS.SERVER.WS)) {
			guid = new WebSphereServiceContext();

		}
		if (PropertyManager.SERVER_TYPE.equals(CONSTANTS.SERVER.NO_SERVER) && uid != null) {
			guid = new JSnooperThreadLocal(uid);
		}
		return guid;
	}

}
