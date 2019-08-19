package net.rationalminds.transfer;

import java.util.HashMap;

import net.rationalminds.dto.CommonTransactionModel;
import net.rationalminds.dto.DataCollectionModel;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;
import net.rationalminds.util.Time;
import net.rationalminds.util.Utilities;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

public class Transport {
    
    private static JSnooperLogger LOGGER = LogFactory.getLogger(Transport.class);
    
    private DataCollectionModel data;
    private boolean errorMark;
    private String aspectName;
    private HashMap<String, Object> objectMap;

    // private E2ETransaction e2eTx;
    /**
     * Constructor for simple transport
     *
     * @param data
     * @param transaction
     */
    public Transport(DataCollectionModel data, String aspectName, String pointCut) {
        this.data = data;
        this.aspectName=aspectName;
        this.data.setMethodSignature(pointCut);
        
        errorMark = false;
        LOGGER.info("TRANSPORT WITH SUCCESS CALLED");
    }

    /**
     * Constructor for error transport
     *
     * @param data
     * @param transaction
     */
    public Transport(DataCollectionModel data, String aspectName, boolean errorMark,
            String pointCut) {
        this.data = data;
        this.aspectName=aspectName;
        this.data.setMethodSignature(pointCut);
        LOGGER.info("TRANSPORT WITH ERROR SETTER CALLED");
    }

    /**
     * Make default constructor as private so that it cannot be directly
     * instantiated
     */
    @SuppressWarnings("unused")
    private Transport() {
    }
    
    public void run() {
//        setData();
        if (errorMark) {
            if (data.getErrorCode() != null) {
                data.setTransactionStatus(CONSTANTS.ERROR);
                dataToQueue();
            }
        } else {
            dataToQueue();
        }
        
    }

    /**
     * Unmarshall the data from captured objects into transport
     *
     * @param obj
     * @param name
     * @return
     */
    private void fetchValue(Object[] obj, String name) {
        int objCount = obj.length;
        objectMap = new HashMap<String, Object>();
        try {
            for (int i = 0; i < objCount; i++) {
                if (obj[i] != null) {
                    objectMap.put(obj[i].getClass().getName(), obj[i]);
                }
            }
            data = new ObjectUnmarshall(data).unmarshall(name, objectMap);
            // LOGGER.TRANSPORT(data.toString());
        } catch (Exception e) {
            LOGGER.error("", e);
        }
    }

    /**
     * Offer transportable object to transport queue
     */
    private void dataToQueue() {
        CommonTransactionModel e2eTx = getTransportObject();
        System.out.println(e2eTx);
    }


    /**
     * Setter function to set value in transportable object
     *
     * @return
     */
    private CommonTransactionModel getTransportObject() {
    	CommonTransactionModel transportModel=new CommonTransactionModel();

    	transportModel.setAction(this.data.getAction());
    	transportModel.setAspectName(this.aspectName);
    	transportModel.setBizTransactionName(this.data.getBiztransactionName());
    	transportModel.setCodeStack(this.data.getCodeStack());
    	transportModel.setErrorCode(this.data.getErrorCode());
    	transportModel.setErrorMessage(this.data.getErrorMessage());
    	transportModel.setGmtTime(this.data.getGmtTime());
    	transportModel.setGuid(this.data.getGuid());
    	transportModel.setKvJasonMap(Utilities.mapToJson(this.data.getKvMap()));
    	transportModel.setLocalPort(this.data.getLocalPort());
    	transportModel.setMethodSignature(this.data.getMethodSignature());
    	transportModel.setRemoteIP(this.data.getRemoteIP());
    	transportModel.setRemoteAddress(this.data.getRemoteAddress());
    	transportModel.setRemotePort(this.data.getRemotePort());
    	transportModel.setTransactionStatus(this.data.getTransactionStatus());
    	
    	
    	transportModel.setLocalMachineName(PropertyManager.HOST_ID);
    	transportModel.setAgentBufferSize(String.valueOf(DataQueue.getDataQueue().size()));
    	transportModel.setAgentBufferLimit(String.valueOf(PropertyManager.POOL_SIZE));
    	transportModel.setTimeZone(Time.getTimeZone());
        return transportModel;
    }
    
}
