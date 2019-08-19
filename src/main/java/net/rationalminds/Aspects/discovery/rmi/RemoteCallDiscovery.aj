package net.rationalminds.Aspects.discovery.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import net.rationalminds.Aspects.JSnooperAspectCore;
import net.rationalminds.dto.EndpointModel;

public abstract aspect RemoteCallDiscovery extends JSnooperAspectCore {

	public pointcut remoteExecution() : within(Remote+) && execution(public * *(..) throws RemoteException);

	public pointcut neglect(): !within(weblogic.jndi..*)
	&& !within(weblogic.messaging.dispatcher.*)
	&& !execution (javax.naming.* *.*(..))
	&& !execution (* *.setSessionContext(..))
	&& !execution (* *.invokeLifecycleMethod(..))
	&& !execution (* *.getJeeInterceptorAdvisors(..))
	&& !execution (* *.getAdvisors(..))
	&& !execution (* *.getTargetSource(..))
	&& !execution (* *.ejbActivate(..))
	&& !execution (* *.ejbPassivate(..))
	&& !execution (* *.ejbLoad(..))
	&& !execution (* *.ejbStore(..))
	&& !execution (* *.ejbRemove(..))
	&& !execution (* *.ejbCreate(..))
	&& !execution (* *.ejbPostCreate(..))
	&& !execution (* *.ejbFindByPrimaryKey(..))
	&& !execution (* *.setEntityContext(..))
	&& !execution (* *.setMessageDrivenContext(..))
	&& !execution (* *.getHandle(..))
	&& !execution (* *.getHomeHandle(..))
	&& !execution (* *.setState(..))
	&& !execution (* *.registerServer())
	&& !execution (weblogic.jms.client.JMSConnection *.connectionCreate(..))
	&& !execution (weblogic.jms.client.JMSConnection *.connectionCreate(..))
	&& !execution (weblogic.jms.client.JMSConnection *.connectionCreateRequest(..));

	public pointcut endPoint() : !within(javax.ejb.EJBObject+) 
		&& !within(javax.ejb.EJBHome+) 
		&& remoteExecution() 
		&& neglect() ;

	/**
	 * 
	 * @param remote
	 */
	before(Remote remote) : endPoint() && this(remote){
		enter();
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		startWithSend(epm);
	}

	/**
	 * 
	 * @param remote
	 */
	after(Remote remote) returning(Object returnObject): endPoint() && this(remote){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm.setReturnObj(returnObject);
		exitWithSuccessAndSend(epm);
		exit();

	}

	/**
	 * 
	 * @param remote
	 */
	after(Remote remote) throwing(Throwable e): endPoint() && this(remote){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		epm.setErrorText(e.getMessage());
		exitWithErrorAndSend(epm);
		exit();

	}

	/**
	 * soft exception message capture
	 * 
	 * @param e
	 */
	/*
	 * before(Exception e):exceptionHandler(e) && cflow(endPoint()){ if (e ==
	 * null) { faults.append("exception variable is null"); } else if
	 * (e.getMessage() == null || e.getMessage().trim().equals("")) {
	 * setSoftError(e); } else if (faults.length() <= 200) {
	 * faults.append(e.getMessage()); } }
	 */

}
