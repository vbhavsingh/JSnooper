package net.rationalminds.Aspects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;

import weblogic.workarea.WorkContextMap;

import net.rationalminds.dto.ConnectionDetails;
import net.rationalminds.dto.DataCollectionModel;
import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.ContextManager;
import net.rationalminds.transaction.JSnooperThreadLocal;
import net.rationalminds.transaction.WebSphereServiceContext;
import net.rationalminds.transaction.web.HttpReception;
import net.rationalminds.transfer.ReflectionUtils;
import net.rationalminds.transfer.Transport;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.HttpUtils;
import net.rationalminds.util.PropertyManager;
import net.rationalminds.util.Time;
import net.rationalminds.util.Utilities;

public abstract aspect JSnooperAspectCore {

	protected pointcut exceptionHandler(Exception e): handler(Exception+) && args(e);

	protected StringBuffer faults = new StringBuffer();

	private DataCollectionModel dataSet = null;

	public String uid = null;

	private JSnooperThreadLocal thread = new JSnooperThreadLocal();

	private ConnectionDetails connDetails = null;
	
	protected HttpReception _read = new HttpReception();

	public pointcut outBoundCall(): call(* java.net.URL.openConnection(..));

	/**
	 * This method just sets the dataSet w/o making any transportation call
	 * 
	 * @param joinPoint
	 * @param resetTime
	 */
	protected void startWithNoSend(EndpointModel epm) {
		collectData(CONSTANTS.START, CONSTANTS.SUCCESS, epm);
	}

	/**
	 * This method just sets the dataSet after exit of method w/o making any
	 * transportation call
	 * 
	 * @param joinPoint
	 * @param resetTime
	 */
	protected void exitWithNoSend(EndpointModel epm) {
		collectData(CONSTANTS.STOP, CONSTANTS.SUCCESS, epm);
	}

	/**
	 * This method sets data and make start call to transport
	 * 
	 * @param joinPoint
	 * @param resetTime
	 */
	protected void startWithSend(EndpointModel epm) {
		collectData(CONSTANTS.START, CONSTANTS.SUCCESS, epm);
		transport(epm);
	}

	/**
	 * Exit method and call transport
	 * 
	 * @param joinPoint
	 * @param returnObj
	 * @param resetTime
	 */
	protected void exitWithSuccessAndSend(EndpointModel epm) {
		collectData(CONSTANTS.STOP, CONSTANTS.SUCCESS, epm);
		transport(epm);
	}

	/**
	 * Exit method with error and call transport
	 * 
	 * @param joinPoint
	 * @param errorText
	 * @param resetTime
	 */
	protected void exitWithErrorAndSend(EndpointModel epm) {
		collectData(CONSTANTS.STOP, CONSTANTS.ERROR, epm);
		transport(epm);
	}

	/**
	 * Set start on Exit of one method call without sending message back to
	 * JSnooper. This functions supports wait cycle for asynchronous or external
	 * calls which goes from parent jvm to other jvm.
	 * 
	 * @param joinPoint
	 * @param returnObj
	 * @param resetTime
	 */
	protected void startOnExitNoSend(EndpointModel epm) {
		collectData(CONSTANTS.START, CONSTANTS.SUCCESS, epm);
	}

	/**
	 * Set start on Exit of one method call and send message back to JSnooper. This
	 * functions supports wait cycle for asynchronous or external calls which
	 * goes from parent jvm to other jvm.
	 * 
	 * @param joinPoint
	 * @param returnObj
	 * @param resetTime
	 */
	protected void startOnExitAndSend(EndpointModel epm) {
		collectData(CONSTANTS.START, CONSTANTS.SUCCESS, epm);
		transport(epm);
	}

	/**
	 * Set start on Exit of one method call with erroneous output and send
	 * message back to JSnooper. This functions supports wait cycle for
	 * asynchronous or external calls which goes from parent jvm to other jvm.
	 * 
	 * @param joinPoint
	 * @param returnObj
	 * @param resetTime
	 */
	protected void startOnErrorAndSend(EndpointModel epm) {
		collectData(CONSTANTS.START, CONSTANTS.ERROR, epm);
		transport(epm);
	}

	/**
	 * Set stop on Exit of one method call without sending message back to
	 * JSnooper. This functions supports wait cycle for asynchronous or external
	 * calls which goes from parent jvm to other jvm.
	 * 
	 * @param joinPoint
	 * @param returnObj
	 * @param resetTime
	 */
	protected void exitOnStartNoSend(EndpointModel epm) {
		collectData(CONSTANTS.STOP, CONSTANTS.SUCCESS, epm);
	}

	/**
	 * Set stop on Exit of one method call and send message back to JSnooper. This
	 * functions supports wait cycle for asynchronous or external calls which
	 * goes from parent jvm to other jvm.
	 * 
	 * @param joinPoint
	 * @param returnObj
	 * @param resetTime
	 */
	protected void exitOnStartAndSend(EndpointModel epm) {
		collectData(CONSTANTS.STOP, CONSTANTS.SUCCESS, epm);
		transport(epm);
	}

	/**
	 * function for marking business erroneous transactions
	 * 
	 * @param joinPoint
	 * @param returnObject
	 */
	protected void errorMarkerWithSucess(EndpointModel epm) {
		collectData(CONSTANTS.STOP, CONSTANTS.ERROR, epm);
		transport(epm, true);
	}
	
	

	/**
	 * Data collection function. This collects time, unique id, transaction
	 * status and action of the pointcut
	 * 
	 * @param action
	 * @param success
	 * @param errorText
	 * @param returnObject
	 * @param resetTime
	 */
	private void collectData(String action, String success, EndpointModel epm) {
		if (dataSet == null) {
			dataSet = new DataCollectionModel();
		}
		if (epm.isResetTime() || this.dataSet.getGmtTime() == null) {
			this.dataSet.setGmtTime(Time.getTime());
		}
		
		//always set new arguments list in dataSet
		dataSet.setObjectList(getObjectList(epm.getJoinPoint()));
		//set returning object
		if(epm.getReturnObj()!=null){
			List<Object> objList=dataSet.getObjectList();
			if(objList==null){
				objList=new ArrayList<Object>();
			}
			objList.add(epm.getReturnObj());
			dataSet.setObjectList(objList);
		}
		/* recollect data when data change is assumed after method completion*/
		if (epm.isRecollectData()) {
			setGuid(action);
			dataSet.setGuid(uid);
			//check if json string is already collected by specific aspects like cxf where
			//request object is not present in joinPoint
			if(epm.getKvMap() != null)
			{
				dataSet.setKvMap(epm.getKvMap());
			}else{
				Map newMap=HttpUtils.insertHttpParamsIntoMap(dataSet.getObjectList());
				dataSet.setKvMap(Utilities.insertIntoMap(dataSet.getKvMap(), newMap));
				//dataSet.ssetKvMap(HttpUtils.getRequestKVMap(dataSet.getObjectList()));
			}
			
			epm=HttpUtils.enrichWithIPPortData(epm, dataSet.getObjectList());
			dataSet.setBiztransactionName(epm.getBizName());
			if (connDetails == null) {
				dataSet.setRemoteAddress(epm.getRemoteMachine());
				dataSet.setRemoteIP(epm.getRemoteIP());
				dataSet.setRemotePort(epm.getRemotePort());
				dataSet.setLocalPort(epm.getLocalPort());
			} else {
				dataSet.setRemoteAddress(connDetails.getRemoteMachine());
				dataSet.setRemoteIP(connDetails.getRemoteIP());
				dataSet.setRemotePort(connDetails.getRemotePort());
				dataSet.setLocalPort(connDetails.getLocalPort());
			}			
		}
		// set end time for transaction
		this.dataSet.setAction(action);
		this.dataSet.setTransactionStatus(success);
		if (epm.getErrorText() == null) {
			this.dataSet.setErrorMessage(faults.toString());
		} else {
			this.dataSet.setErrorMessage(epm.getErrorText());
		}
	}
	

	/**
	 * Call to transport class
	 * 
	 * @param aspect
	 * @param joinPoint
	 */
	private void transport(EndpointModel epm) {
		String aspectName = this.getClass().getName();
		String signature = epm.getJoinPoint().getSignature().toLongString();
		Transport transport = new Transport(this.dataSet, aspectName, signature);
		transport.run();
	}

	/**
	 * Call transport for error specific functions. The error marker function
	 * may be use for setting error codes
	 * 
	 * @param aspect
	 * @param joinPoint
	 * @param errorMarked
	 */
	private void transport(EndpointModel epm, boolean errorMarked) {
		String aspectName = this.getClass().getName();
		String signature = epm.getJoinPoint().getSignature().toLongString();
		Transport transport = new Transport(this.dataSet, aspectName, errorMarked, signature);
		transport.run();
	}

	/**
	 * This block traces soft errors from the execution blocks.
	 */
	protected void setSoftError(Throwable e) {
		if (faults.length() <= 200) {
			String errorString = e.getStackTrace()[0].toString();
			if (!faults.toString().contains(errorString))
				faults.append("error " + errorString);
		}
	}

	/**
	 * Increment counter for guid whenever method is entered
	 */
	protected void enter() {
		thread.get().entry();
	}

	/**
	 * Decrement counter for guid when method is completed. Check if counter is
	 * 0 remove guid from thread.
	 */
	protected void exit() {
		thread.get().exit();
		if (thread.get().getCounter() == 0) {
			thread.remove();
		}
	}

	/**
	 * access thread local variable
	 * 
	 * @return
	 */
	public JSnooperThreadLocal getThreadLocal() {
		return this.thread;
	}

	/**
	 * 
	 */
	private void setGuid(String action) {
		if (PropertyManager.SERVER_TYPE.equals(CONSTANTS.SERVER.WLS)) {
			if (uid == null || action.equals(CONSTANTS.START)) {
				ContextManager ctx = new ContextManager();
				WorkContextMap map = ctx.getContext();
				if (map != null) {
					uid = map.get(CONSTANTS.GUID).toString();
				}
			}
		}
		if (PropertyManager.SERVER_TYPE.equals(CONSTANTS.SERVER.WS)) {
			WebSphereServiceContext ctx = new WebSphereServiceContext();
			String _temp = ctx.getGuid(action);
			if (_temp != null) {
				uid = _temp;
			}
		}
		if (PropertyManager.SERVER_TYPE.equals(CONSTANTS.SERVER.NO_SERVER)) {
			if (uid == null || action.equals(CONSTANTS.START)) {
				if (thread.get() != null) {
					uid = thread.get().getGuid();
				}
			}
		}
	}

	/**
	 * This method is used by implementor aspects to forward the UID over HTTP
	 * channels
	 * 
	 * @param conn
	 */
	public void forwardGuid(Object conn) {
		if (ReflectionUtils.instanceOf("java.net.HttpURLConnection", conn)) {
			forwardGuidImpl(conn);
		}
		if (ReflectionUtils.instanceOf(
				"sun.net.www.protocol.http.HttpURLConnection", conn)) {
			forwardGuidImpl(conn);
		}
	}

	/**
	 * Implementation of @forwardGuid method
	 * 
	 * @param conn
	 */
	public void forwardGuidImpl(Object conn) {
		java.net.URLConnection c = (java.net.URLConnection) conn;
		if (connDetails == null) {
			connDetails = new ConnectionDetails();
		}
		connDetails.setRemoteMachine(c.getURL().getHost());
		connDetails.setRemotePort(String.valueOf(c.getURL().getPort()));
		c.addRequestProperty(CONSTANTS.GUID, getThreadLocal().get().getGuid());
	}
	
	/**
	 * Extract arguments from joinpoint and add it to returned list
	 * @param jp
	 * @return 
	 */
	public List<Object> getObjectList(JoinPoint jp){
		List<Object> objectList = new ArrayList<Object>();
		objectList.add(jp.getThis());
		Object temp[] = jp.getArgs();

		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				objectList.add(temp[i]);
			}
		}
		return objectList;
	}
}
