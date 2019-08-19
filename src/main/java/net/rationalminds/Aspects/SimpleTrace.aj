package net.rationalminds.Aspects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import net.rationalminds.aspect.annotation.AnnotationExtension;

import org.aspectj.lang.JoinPoint;



public abstract aspect SimpleTrace {

	 // public pointcut run(): within(HttpServlet+) && !within(HttpJspPage+)
		// && (execution(* HttpServlet.do*(..)) || execution(*
		// HttpServlet.service(..))) && !within(javax..*);


	 /*
		 * public pointcut runTrace():within(javax.xml.rpc.handler.Handler+) &&
		 * execution(* *.*(..)) && !within(com.bea.logging.*) &&
		 * !within(weblogic.logging.*);
		 */
	
	 public pointcut runTrace():execution(* *(..))
	 && !execution(boolean *(..))
	 && !execution(java.lang.Boolean *(..))
	 && !execution(void *.set*(..))
	 && !execution(* *.get*(..))
	 && !execution(* *.debug*(..))
	 && !execution(* *.error*(..))
	 && !execution(* *.log*(..))
	 && !execution(* *.Log*(..))
	 && !execution(* *.equals(..))
	 && !execution(* *.getClass(..))
	 && !execution(* *.notify(..))
	 && !execution(* *.hashCode(..))
	 && !execution(* *.notifyAll(..))
	 && !execution(* *.clone(..))
	 && !execution(* *.*Log*(..))
	 && !within(org.apache.commons..*)
	 && !within(java..*)
	 && !within(javax..*)
	 && !within(com.ibm.websphere.security..*)
	 && !within(com.ibm.websphere.cluster..*)
	 && !within(org.springframework..*)
	 && !within(com.e2e..*)
	  && !within(org.apache.xmlbeans..*)
	  && !within(org.hibernate..*)
	  && !within(org.apache.log4j..*)
	  && !within(*..util..*)
	  && !within(net.sf..*)
	  && !within(com.rsa..*)
	  && !within(com.certicom..*)
	  && !within(com.octetstring..*)
	  && !within(org.jboss.aop..*)
	  && !within(org.aspectj..*)
	  && !within(org.apache.catalina..*)
	  && !within(org.apache.naming..*)
	  && !within(com.ibm.jdbcx..*)
	  && !within(com.ibm.ejs..*)
	  && !within(org.eclipse..*)
	  && !within(org.osgi..*)
	  && !within(org.osgi..*)
	  && !within(com.amdocs.config..*)
	  && !within(*..xml..*)
	  && !within(amdocs.epi.xml..*)
	 && !within(java.io.ObjectInputStream+);
	


	before(): runTrace(){
		System.out.println(thisJoinPoint.toLongString()+ " ***JSNOOPER***");
		
		Object obj=thisJoinPoint.getThis();
		
		if(obj!=null){
		
		 Object args[]=thisJoinPoint.getArgs();
		 for (int i = 0; i < args.length; i++) { 
			 System.out.println(args[i].getClass().getName()+"::"+args[i].toString()+"Argument"); 
		 }
		 
			
		  System.out.print(obj.getClass().getCanonicalName() +"*AJT*" + "\n"); 
		  Method m[] =obj.getClass().getMethods();
		  
		  Annotation a[] = obj.getClass().getAnnotations();
		  
		  for (int i = 0; i < m.length; i++) { System.out.println("Method: " +
		  m[i].getName()); }
		  
		  for (int i = 0; i < a.length; i++) { System.out.println("Annotaion: " +
		  a[i].annotationType().getName()); }
		  
		  Class itf[] = obj.getClass().getInterfaces();
		  
		  for (int i = 0; i < itf.length; i++) { System.out.println("Interface: " +
		  itf[i].getCanonicalName()); }
		  
		  Class sp = thisJoinPoint.getThis().getClass().getSuperclass();
		  
		  System.out.println("SuperClass: " + sp.getCanonicalName());
		  
		  System.out.println("Target:" +
		  thisJoinPoint.getTarget().getClass().getCanonicalName());
		}
		
		 
	}

}
