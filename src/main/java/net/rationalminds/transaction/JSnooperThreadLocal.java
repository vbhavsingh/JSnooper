package net.rationalminds.transaction;

import net.rationalminds.transaction.guid.GUID;
import net.rationalminds.transaction.guid.GUIDPropgation;

public class JSnooperThreadLocal implements GUIDPropgation {
	
	

	private static InheritableThreadLocal<GUID> threadUniqueID = new InheritableThreadLocal<GUID>() {

		@Override
		protected GUID initialValue() {
			GUID id = new GUID();

			return id;
		}
	};
	
	

	public JSnooperThreadLocal(String initValue) {
		GUID id=get();
		id.setGuid(initValue);
	}

	public JSnooperThreadLocal() {
		super();
	}
	
	public void set(GUID id) {

		threadUniqueID.set(id);
	}

	public GUID get() {
		return (GUID) threadUniqueID.get();
	}

	public void remove() {
		threadUniqueID.remove();
	}

	public String getCorrelator() {
		return ((GUID) get()).getGuid();
	}

	public void setCorrelator(String value) {
		GUID id = (GUID) get();
		if (id == null) {
			id = new GUID();
			id.setGuid(value);
		}
		id.setGuid(value);
		set(id);

	}

}
