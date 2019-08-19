package net.rationalminds.util;

public class CONSTANTS {

    /* Version flag for agent */
    public static final String JSNOOPER = "jsnooper.profile";

    /* Version flag for agent */
    public static final String JSNOOPER_VERSION = "1.0";

    /* name of the property file */
    public static final String PROP_FILE = "ProbeProfile.properties";

    /* name of the property file */
    public static final String CONFIG_FILE = ".version";

    /* name of the aspect j file */
    public static final String AOP_FILE = "META-INF/aop-ajc.xml";

    /* Exception read character length */
    public static int READ_CAPACITY = 200;

    /*-D parameter name string for data transport mode*/
    public static final String TRANSPORT_MODE = "jsnooper.transport.mode";

    /*-D parameter name string for maximum consumer threads*/
    public static final String MAX_THREAD_COUNT = "jsnooper.max.thread.count";

    /*-D parameter name string for minimum consumer threads*/
    public static final String MIN_THREAD_COUNT = "jsnooper.min.thread.count";

    /**
     * values of different transport modes
     */
    public static final String TRANSPORT_WS = "ws";
    public static final String TRANSPORT_SERVLET = "servlet";
    public static final String TRANSPORT_JMS = "jms";

    /* Property for middle data pool in jsnooper */
    public static final String DATA_POOL_SIZE = "jsnooper.data.poolsize";

    /* GUID name key */
    public static final String GUID = "j_snooper_G_uid_Val";
    public static final String EXCEPTION = "j_snooper_G_uid_exception";
    public static final String WS_PARTITION = "j_snooper_end_to_end";
    public static final String WS_WORKAREA = "j_snooper_end_to_end_id";

    /* START flag value */
    public static final String START = "START";

    /* STOP falg value */
    public static final String STOP = "STOP";

    /* Sucess transaction marker value */
    public static final String SUCCESS = "1";

    /* failed transaction marker value */
    public static final String ERROR = "0";

    /**
     * if servlet transport mode is opted. Default number of pooled http
     * connections are defined.
     */
    public static final short HTTP_CONNECTION = 15;

    public static final String ACTION = "action";

    public static final String GMT_TIME = "gmtTime";

    public static final String METHOD_SIGNATURE = "methodSignature";

    public static final String THIS_MACHINE_NAME = "localMachineName";

    public static final String TZ = "timeZone";

    public static final String AGENT_NAME = "agentName";

    public static final String APP_NAME = "applicationName";

    public static final String BIZ_TRANSACTION_ID = "bizTransactionId";

    public static final String TRANSACTION_ID = "guid";

    public static final String REMOTE_IP = "remoteIP";

    public static final String REMOTE_PORT = "remotePort";

    public static final String LOCAL_PORT = "localPort";

    public static final String CODE_STACK = "codeStack";

    public static final String KV_MAP = "kvJasonMap";

    public static final String ERROR_CODE = "errorCode";

    public static final String ERROR_MESSAGE = "errorMessage";

    public static final String TRANSACTION_STATUS = "transactionStatus";

    public static final String ASPECT_NAME = "aspectName";

    public static final String AGENT_BUFFER_SIZE = "agentBufferSize";

    public static final String AGENT_BUFFER_LIMIT = "agentBufferLimit";

    /**
     * {@link} Server inner class maintains the names of various server on which
     * the agent would be deployed.
     *
     * @author Vaibhav Singh
     *
     */
    public static final class SERVER {
        /*-D parameter name string for defining server type*/

        public static final String SERVER_TYPE = "jsnooper.server.type";
        /**
         * acceptable values of different server types.
         */
        public static final String WLS = "weblogic";
        public static final String WS = "websphere";
        public static final String TOMCAT = "apache_tomcat";
        public static final String IPLANET = "iplanet";
        public static final String GERMINO = "germino";
        public static final String IIS = "iis";
        public static final String ARTIX = "artix";
        public static final String DEFAULT = "na";
        public static final String NO_SERVER = "na";
    }

    /**
     * {@link} TECH inner class maintains the technologies that would be used
     * for guid propagation.
     *
     * @author Vaibhav Singh
     *
     */
    public static final class TECH {

        public static final String TECH = "jsnooper.prop.tech";
        public static final String JMS = "jms";
        public static final String HTTP = "http";
        public static final String EJB = "ejb";
        public static final String RMI = "rmi";
        public static final String WEB_SERVICE_HTTP = "soap_http";
        public static final String WEB_SERVICE_RMI = "soap_rmi";
        public static final String JAX_WS_JAVA6 = "jaxws6";
        public static final String JAX_RPC = "jaxrpc";
        public static final String CXF_WS = "cxf";
        public static final String APACHE = "apache";
        public static final String UNKNOWN = "ND";
    }

    /**
     * {@link} CONTROL inner class maintains the control flags for agent. Theses
     * control flags would be updated from webservices to control various aspect
     * of agent functionality from external interface.
     *
     * @author Vaibhav Singh
     *
     */
    public static class CONTROL {
        /*
         * This flag controls the data collection from agent. if false advice
         * body would not be exceuted
         */

        public static boolean PROBE_ON = true;

        public static boolean JMX_STATUS = false;
    }

    /**
     * Log file configuration key parameters
     *
     * @author Vaibhav Singh
     *
     */
    public static class LOG {

        /*-D parameter name string for log status*/
        public static String LOG_LEVEL = "jsnooper.log.level";

        /*-D parameter name string for log mode for choosing between log4j and SOP statements*/
        public static String LOG_APPENDER = "jsnooper.log.appender";

        /*-D param for jsnooper log file path*/
        public static String LOG_PATH = "jsnooper.log.path";

        /* property key for jsnooper log file max size */
        public static String LOG_SIZE = "jsnooper.log.max.size";
        public static String LOG_SIZE_DEFAULT = "5MB";

        /* property key for jsnooper log file archive size */
        public static String LOG_ARCHIVE_SIZE = "jsnooper.log.archive.size";
        public static int LOG_ARCHIVE_SIZE_DEFAULT = 5;

        /**
         * Log status values as -D parameter argument
         */
        public static final String LOG_APPENDER_DEFAULT = "sop";
        public static final String LOG_LEVEL_NOLOG = "nolog";
        public static final String LOG_LEVEL_FULL = "logall";
        public static final String LOG_LEVEL_ERROR = "error";
        public static final String LOG_LEVEL_WARN = "warn";
        public static final String LOG_LEVEL_TRACE = "trace";
        public static final String LOG_LEVEL_FATAL = "fatal";
        public static final String LOG_LEVEL_SEVERE = "severe";
        public static final String LOG_LEVEL_INFO = "others";

    }
}
