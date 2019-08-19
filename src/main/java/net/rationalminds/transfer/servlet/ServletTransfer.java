package net.rationalminds.transfer.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.rationalminds.apache.http.client.HttpClient;
import net.rationalminds.apache.http.client.entity.UrlEncodedFormEntity;
import net.rationalminds.apache.http.client.methods.HttpPost;
import net.rationalminds.apache.http.conn.ClientConnectionManager;
import net.rationalminds.apache.http.conn.params.ConnManagerParams;
import net.rationalminds.apache.http.conn.params.ConnPerRouteBean;
import net.rationalminds.apache.http.conn.scheme.PlainSocketFactory;
import net.rationalminds.apache.http.conn.scheme.Scheme;
import net.rationalminds.apache.http.conn.scheme.SchemeRegistry;
import net.rationalminds.apache.http.impl.client.DefaultHttpClient;
import net.rationalminds.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import net.rationalminds.core.apache.http.HttpEntity;
import net.rationalminds.core.apache.http.HttpResponse;
import net.rationalminds.core.apache.http.NameValuePair;
import net.rationalminds.core.apache.http.message.BasicNameValuePair;
import net.rationalminds.core.apache.http.params.BasicHttpParams;
import net.rationalminds.core.apache.http.params.HttpParams;
import net.rationalminds.core.apache.http.protocol.HTTP;
import net.rationalminds.dto.CommonTransactionModel;
import net.rationalminds.transfer.TransferItf;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

public class ServletTransfer implements TransferItf {

    private static JSnooperLogger LOGGER = LogFactory
            .getLogger(ServletTransfer.class);

    /* URL specified in property file */
    private static String URL;

    private static HttpClient client;

    private CommonTransactionModel data;

    private static ClientConnectionManager connMgr;

    /* Flag to check whether connection should be retried */
    private static boolean isServletUp = true;

    /**
     * static block initiates the reconnect logic as Timer task. It also
     * initializes the HttpClient Module and http connection pool
     */
    static {
        URL = PropertyManager.getVal("SERVLET_URL");
        try {
            setClient();
            reconnect();
        } catch (Exception e) {
            reconnect();
            LOGGER.error("UNABLE TO INITLIZE SERVLET", e);
        }
    }

    /**
     * Constructor call takes E2ETransaction object as parameter
     *
     * @param e2eTx
     */
    public ServletTransfer(CommonTransactionModel e2eTx) {
        this.data = e2eTx;
    }

    /**
     * Overloading run of Runnable interface which in turn is inherited by
     * TransferItf interface. This run executes posting of message to servlet.
     * In case of any error it shuts down the agent.
     */
    public void run() {
        try {
            postRequest();
        } catch (Exception e) {
            LOGGER
                    .error("SERVLET TRANSPORT FAILED: "
                            + e.getLocalizedMessage());
            e.printStackTrace();
            CONSTANTS.CONTROL.PROBE_ON = false;
            isServletUp = false;
        }
    }

    /**
     * Sets HttpClient and HttpClientConnectingManager parameters
     *
     * @throws Exception
     */
    private static void setClient() throws Exception {
        HttpParams params = new BasicHttpParams();
        // Increase max total connection to 200
        ConnManagerParams.setMaxTotalConnections(params, 200);
        ConnManagerParams.setTimeout(params, 2000);

        // Increase default max connection per route to 20
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(200);
        ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));

        connMgr = new ThreadSafeClientConnManager(params, schemeRegistry);
        client = new DefaultHttpClient(connMgr, params);
        LOGGER.info("CONNECTED TO URL: " + URL);

    }

    /**
     * Post request to servlet
     *
     * @throws Exception
     */
    public void postRequest() throws Exception {
        HttpPost post = new HttpPost(URL);
        LOGGER.info("Creating Servlet Request");
        List<NameValuePair> nvPair = new ArrayList<NameValuePair>();

        nvPair.add(new BasicNameValuePair(CONSTANTS.ACTION, data.getAction()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.GMT_TIME, data.getGmtTime()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.METHOD_SIGNATURE, data.getMethodSignature()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.THIS_MACHINE_NAME, data.getLocalMachineName()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.TZ, data.getTimeZone()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.AGENT_NAME, data.getAgentName()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.APP_NAME, data.getApplicationName()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.BIZ_TRANSACTION_ID, data.getBizTransactionName()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.TRANSACTION_ID, data.getGuid()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.REMOTE_IP, data.getRemoteIP()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.REMOTE_PORT, data.getRemotePort()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.LOCAL_PORT, data.getLocalPort()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.ERROR_CODE, data.getErrorCode()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.ERROR_MESSAGE, data.getErrorMessage()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.TRANSACTION_STATUS, data.getTransactionStatus()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.CODE_STACK, data.getCodeStack()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.KV_MAP, data.getKvJasonMap()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.ASPECT_NAME, data.getAspectName()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.AGENT_BUFFER_SIZE, data.getAgentBufferSize()));
        nvPair.add(new BasicNameValuePair(CONSTANTS.AGENT_BUFFER_LIMIT, data.getAgentBufferLimit()));

        try {
            HttpEntity entity = new UrlEncodedFormEntity(nvPair, HTTP.UTF_8);
            post.setEntity(entity);
            LOGGER.info("Servlet request: " + HttpPost.METHOD_NAME);
            HttpResponse response = client.execute(post);
            entity = response.getEntity();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    entity.getContent()));
            reader.close();

        } catch (Exception e) {
            LOGGER.error("SERVLET TRANSPORT FAILED..ABORTING SERVICES", e);
            post.abort();
            client.getConnectionManager().shutdown();
            throw e;
        }

    }

    /**
     * When error is encountered while posting the request, agent sleeps for
     * configured time. Reconnect tries connection periodically, and if
     * connection is reestablished it turns on the agent again.
     */
    private static void reconnect() {
        try {

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    try {
                        if (!isServletUp) {
                            setClient();
                            isServletUp = true;
                            CONSTANTS.CONTROL.PROBE_ON = true;
                            LOGGER.warn("Reconnecting Servlet...");
                        }
                    } catch (Exception e) {
                        LOGGER.error("Servlet Post failed...Aborting", e);
                        CONSTANTS.CONTROL.PROBE_ON = false;
                        isServletUp = false;
                    }
                }

            }, new Date(), 15 * 60 * 1000);

        } catch (Exception ex) {
            LOGGER.error("", ex);
        }
    }
}
