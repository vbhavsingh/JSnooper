# JSnooper

JSnooper is a jvm transaction monitoring agent. It tracks transactions in distributed environment by silently genereating and inserting a unique id into every transaction.
The unique is preserved inside JVM using InheritableThreadLocal <java.lang.InheritableThreadLocal>.
The mechnism works as following for any JVM installation:

1. When transaction enters in JVM using application protocol HTTP (Servlet/ Web service(JAX-WS, JAX-RS)) or RMI/IIOP (JMS), interceptors check for unique id, if unique id is not present, generate new id. Preserve id in ThreadLocal.
2. When outward call is made to third system, fetch unique id from ThreadLocal and insert into placeholders at application transport protocol level.

One important point w.r.t ThreadLocal is that application servers like Weblogic and Websphere provide special context holders, which is used by the agent if deployed on relevant server instead of ThreadLocal.

The agent is written in AspectJ and is plugged at runtime in JVM using JVMTI. The supported Java based transport mechanisms are listed as below.

1. Spring 
2. JAXRS
3. Struts 1
4. Struts 2
5. JSF
6. VELOCITY
7. Apache CXF
8. Jersey
9. Jetty
10. JMS

Weblogic and Websphere servers are supportrd out of box. All other server technologies are considered as standalone JAVA applications.

Jsnooper collects following transaction iinformation as part of its monitoring.
1. Tracked method signatrure.
2. Type of method as per its technology.
3. Action : {Entry/ Start or Exit/ End}
4. Timestamp in GMT
5. Origination IP Address of incoming transaction
6. Originating port of incmoing transaction 
7. Local port number at which transaction was recieved.
8. Local IP address 
9. Stacktrace in case of exception
10. Key value map of data objects from arguments and return of method.
