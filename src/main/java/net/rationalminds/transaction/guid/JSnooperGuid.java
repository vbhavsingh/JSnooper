package net.rationalminds.transaction.guid;


public interface JSnooperGuid extends java.rmi.Remote{
	public void propogateGuid(String guid) throws java.rmi.RemoteException;

}
