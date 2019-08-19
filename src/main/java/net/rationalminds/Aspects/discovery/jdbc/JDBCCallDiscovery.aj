package net.rationalminds.Aspects.discovery.jdbc;

import net.rationalminds.Aspects.JSnooperAspectCore;
import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transfer.ReflectionUtils;
import net.rationalminds.util.CONSTANTS;

public abstract aspect JDBCCallDiscovery extends JSnooperAspectCore {
	
	
	// Transaction start point cut
	public abstract pointcut pointCut();
	
	private pointcut avoid():execution(* java.sql.Statement.execute(..)) 
	|| execution(* java.sql.Statement.executeQuery(..)) 
	|| execution(* java.sql.Statement.executeUpdate(..)) 
	|| execution(* java.sql.Connection.prepareStatement(..)) ;

	/**
	 * Operations before beginPoint Set mthe start time for execution
	 */
	
	before(): pointCut() && !cflowbelow(avoid()) && if(CONSTANTS.CONTROL.PROBE_ON){
		enter();
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, true);
		
		//get jdbc url and user name
		Object target = thisJoinPoint.getTarget();
		
		//get sql from input argumemt
		Object temp[] = thisJoinPoint.getArgs();
		//arguments will not be present for no argument execute method
		//use previous sql for no argument execute method
		if (temp != null && temp.length > 0) 
		{
			if(temp[0] != null)
			{
				epm.getKvMap().put("BIZ.JDBC.QUERY", temp[0].toString());
			}
		}
//		else{
//			epm = new EndpointModel(thisJoinPoint, true, true);
//			//this must be preparedstatement call
//			//try if prepraredStatement object has it
//			if(target != null){
//				epm.getKvMap().put("BIZ.JDBC.QUERY", target.toString());
//			}
//		}
		
		//target can be statement or connection
		if(target != null)
		{
			Object connection = null;
			if(ReflectionUtils.instanceOf("java.sql.Statement", target)){
				connection = ReflectionUtils.invokeMethodObject("getConnection", target);
			}else if(ReflectionUtils.instanceOf("java.sql.Connection", target))
			{
				connection = target;
			}
			if(connection != null)
			{
				//get Metadata to get url and user info
				Object dbmetaData = ReflectionUtils.invokeMethodObject("getMetaData",connection);
				if(dbmetaData != null)
				{
					epm.getKvMap().put("BIZ.JDBC.URL",(String)ReflectionUtils.invokeMethodObject("getURL",dbmetaData));
					//call to user name starts recursive call
					//epm.getKvMap().put("BIZ.JDBC.USERNAME",(String)ReflectionUtils.invokeMethodObject("getUserName",dbmetaData));
				}
			}
		}	
		startWithSend(epm);
	}

	/**
	 * Operations after endPoint set end time for complete transaction
	 */
	after() returning(Object returnObject): pointCut() && !cflowbelow(avoid()) &&  if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, false);
		epm.setReturnObj(returnObject);
		exitWithSuccessAndSend(epm);
		exit();
	}

	/**
	 * Exception Handling
	 */
	after()throwing(Throwable e):pointCut() &&  !cflowbelow(avoid())  &&  if(CONSTANTS.CONTROL.PROBE_ON){
		EndpointModel epm = new EndpointModel(thisJoinPoint, true, false);
		epm.setErrorText(e.getMessage());
		exitWithErrorAndSend(epm);
		exit();
	}
	

}