package net.rationalminds.transaction.guid;


public class GUID {
	
	private String guid;
	
	private int counter=0;
	
	public GUID(){
		this.guid=UID.get();
	}
	
	public GUID(String guid){
		this.guid=guid;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public void entry(){
		counter++;
	}
	
	public void exit(){
		counter--;
	}

}
