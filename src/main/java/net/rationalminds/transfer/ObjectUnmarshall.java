package net.rationalminds.transfer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.HashMap;

import net.rationalminds.dto.CommonTransactionModel;
import net.rationalminds.dto.DataCollectionModel;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;
import net.rationalminds.util.LogManager.JSnooperLogger;
import net.rationalminds.util.LogManager.LogFactory;

import java.util.Map;

public class ObjectUnmarshall {

    private static JSnooperLogger LOGGER = LogFactory
            .getLogger(ObjectUnmarshall.class);

    private DataCollectionModel data;

    private Map<String, String> dataMap = new HashMap<String, String>();

    public ObjectUnmarshall(DataCollectionModel data) {
        this.data = data;
    }

    public DataCollectionModel unmarshall(String name, HashMap<String, Object> objectMap)
            throws Exception {
        if (data == null) {
            LOGGER.error("Object Unmarshalling encountered null", null);
        }
        String methodMsisdn = PropertyManager.getVal(name + "_METHOD_MSISDN");
        if (methodMsisdn != null && methodMsisdn.contains(",")) {
            String s[] = methodMsisdn.split(",");
           // data.setMsisdn(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodImsi = PropertyManager.getVal(name + "_METHOD_IMSI");
        if (methodImsi != null && methodImsi.trim().contains(",")) {
            String s[] = methodImsi.split(",");
        //    data.setImsi(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodImei = PropertyManager.getVal(name + "_METHOD_IMEI");
        if (methodImei != null && methodImei.trim().contains(",")) {
            String s[] = methodImei.split(",");
         //   data.setImei(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodMkt = PropertyManager.getVal(name + "_METHOD_MARKET_CODE");
        if (methodMkt != null && methodMkt.trim().contains(",")) {
            String s[] = methodMkt.split(",");
          //  data.setMarketID(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodIccid = PropertyManager.getVal(name + "_METHOD_ICCID");
        if (methodIccid != null && methodIccid.trim().contains(",")) {
            String s[] = methodIccid.split(",");
         //   data.setIccID(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodErrorCode = PropertyManager.getVal(name
                + "_METHOD_ERROR_CODE");
        if (methodErrorCode != null && methodErrorCode.trim().contains(",")) {
            String s[] = methodErrorCode.split(",");
        //    data.setErrorCode(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodErrorText = PropertyManager.getVal(name
                + "_METHOD_ERROR_TEXT");
        if (methodErrorText != null && methodErrorText.trim().contains(",")) {
            String s[] = methodErrorText.split(",");
        //    data.setErrorText(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String guid = PropertyManager.getVal(name + "_METHOD_GUID");
        if (guid != null && guid.trim().contains(",")) {
            String s[] = guid.split(",");
        //    data.setGuid(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String ban = PropertyManager.getVal(name + "_METHOD_BAN");
        if (ban != null && ban.trim().contains(",")) {
            String s[] = ban.split(",");
        //    data.setBan(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField1 = PropertyManager.getVal(name + "_METHOD_FIELD1");
        if (methodField1 != null && methodField1.trim().contains(",")) {
            String s[] = methodField1.split(",");
         //   data.setField1(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField2 = PropertyManager.getVal(name + "_METHOD_FIELD2");
        if (methodField2 != null && methodField2.trim().contains(",")) {
            String s[] = methodField2.split(",");
         //   data.setField2(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField3 = PropertyManager.getVal(name + "_METHOD_FIELD3");
        if (methodField3 != null && methodField3.trim().contains(",")) {
            String s[] = methodField3.split(",");
         //   data.setField3(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField4 = PropertyManager.getVal(name + "_METHOD_FIELD4");
        if (methodField4 != null && methodField4.trim().contains(",")) {
            String s[] = methodField4.split(",");
         //   data.setField4(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField5 = PropertyManager.getVal(name + "_METHOD_FIELD5");
        if (methodField5 != null && methodField5.trim().contains(",")) {
            String s[] = methodField5.split(",");
        //    data.setField5(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField6 = PropertyManager.getVal(name + "_METHOD_FIELD6");
        if (methodField6 != null && methodField6.trim().contains(",")) {
            String s[] = methodField6.split(",");
         //   data.setField6(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField7 = PropertyManager.getVal(name + "_METHOD_FIELD7");
        if (methodField7 != null && methodField7.trim().contains(",")) {
            String s[] = methodField7.split(",");
        //    data.setField7(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField8 = PropertyManager.getVal(name + "_METHOD_FIELD8");
        if (methodField8 != null && methodField8.trim().contains(",")) {
            String s[] = methodField8.split(",");
        //    data.setField8(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField9 = PropertyManager.getVal(name + "_METHOD_FIELD9");
        if (methodField9 != null && methodField9.trim().contains(",")) {
            String s[] = methodField9.split(",");
        //    data.setField9(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField10 = PropertyManager.getVal(name + "_METHOD_FIELD10");
        if (methodField10 != null && methodField10.trim().contains(",")) {
            String s[] = methodField10.split(",");
         //   data.setField10(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField11 = PropertyManager.getVal(name + "_METHOD_FIELD11");
        if (methodField11 != null && methodField11.trim().contains(",")) {
            String s[] = methodField11.split(",");
        //    data.setField11(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField12 = PropertyManager.getVal(name + "_METHOD_FIELD12");
        if (methodField12 != null && methodField12.trim().contains(",")) {
            String s[] = methodField12.split(",");
        //    data.setField12(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField13 = PropertyManager.getVal(name + "_METHOD_FIELD13");
        if (methodField13 != null && methodField13.trim().contains(",")) {
            String s[] = methodField13.split(",");
        //    data.setField13(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField14 = PropertyManager.getVal(name + "_METHOD_FIELD14");
        if (methodField14 != null && methodField14.trim().contains(",")) {
            String s[] = methodField14.split(",");
        //    data.setField14(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField15 = PropertyManager.getVal(name + "_METHOD_FIELD15");
        if (methodField15 != null && methodField15.trim().contains(",")) {
            String s[] = methodField15.split(",");
         //   data.setField15(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField16 = PropertyManager.getVal(name + "_METHOD_FIELD16");
        if (methodField16 != null && methodField16.trim().contains(",")) {
            String s[] = methodField16.split(",");
         //   data.setField16(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField17 = PropertyManager.getVal(name + "_METHOD_FIELD17");
        if (methodField17 != null && methodField17.trim().contains(",")) {
            String s[] = methodField17.split(",");
          //  data.setField17(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField18 = PropertyManager.getVal(name + "_METHOD_FIELD18");
        if (methodField18 != null && methodField18.trim().contains(",")) {
            String s[] = methodField18.split(",");
       //     data.setField18(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField19 = PropertyManager.getVal(name + "_METHOD_FIELD19");
        if (methodField19 != null && methodField19.trim().contains(",")) {
            String s[] = methodField19.split(",");
        //    data.setField19(invokeMethod(s[0], objectMap.get(s[1])));
        }

        String methodField20 = PropertyManager.getVal(name + "_METHOD_FIELD20");
        if (methodField20 != null && methodField20.trim().contains(",")) {
            String s[] = methodField20.split(",");
        //    data.setField20(invokeMethod(s[0], objectMap.get(s[1])));
        }

        /**
         * Read possible data values from object fields
         */
        String fieldMsisdn = PropertyManager.getVal(name + "_FIELD_MSISDN");
        if (fieldMsisdn != null && fieldMsisdn.contains(",")) {
            String s[] = fieldMsisdn.split(",");
            if (s.length > 2) {
            //    data.setMsisdn(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setMsisdn(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldImsi = PropertyManager.getVal(name + "_FIELD_IMSI");
        if (fieldImsi != null && fieldImsi.trim().contains(",")) {
            String s[] = fieldImsi.split(",");
            if (s.length > 2) {
             //   data.setImsi(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
             //   data.setImsi(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldImei = PropertyManager.getVal(name + "_FIELD_IMEI");
        if (fieldImei != null && fieldImei.trim().contains(",")) {
            String s[] = fieldImei.split(",");
            if (s.length > 2) {
            //    data.setImei(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setImei(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldMkt = PropertyManager.getVal(name + "_FIELD_MARKET_CODE");
        if (fieldMkt != null && fieldMkt.trim().contains(",")) {
            String s[] = fieldMkt.split(",");
            if (s.length > 2) {
            //    data.setMarketID(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
             //   data.setMarketID(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldIccid = PropertyManager.getVal(name + "_FIELD_ICCID");
        if (fieldIccid != null && fieldIccid.trim().contains(",")) {
            String s[] = fieldIccid.split(",");
            if (s.length > 2) {
            //    data.setIccID(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
             //   data.setIccID(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldErrorCode = PropertyManager.getVal(name
                + "_FIELD_ERROR_CODE");
        if (fieldErrorCode != null && fieldErrorCode.trim().contains(",")) {
            String s[] = fieldErrorCode.split(",");
            if (s.length > 2) {
            //    data.setErrorCode(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setErrorCode(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldErrorText = PropertyManager.getVal(name
                + "_FIELD_ERROR_TEXT");
        if (fieldErrorText != null && fieldErrorText.trim().contains(",")) {
            String s[] = fieldErrorText.split(",");
            if (s.length > 2) {
            //    data.setErrorText(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setErrorText(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldGuid = PropertyManager.getVal(name + "_FIELD_GUID");
        if (fieldGuid != null && fieldGuid.trim().contains(",")) {
            String s[] = fieldGuid.split(",");
            if (s.length > 2) {
                data.setGuid(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
                data.setGuid(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldBan = PropertyManager.getVal(name + "_FIELD_BAN");
        if (fieldBan != null && fieldBan.trim().contains(",")) {
            String s[] = fieldBan.split(",");
            if (s.length > 2) {
           //     data.setBan(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setBan(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField1 = PropertyManager.getVal(name + "_FIELD_FIELD1");
        if (fieldField1 != null && fieldField1.trim().contains(",")) {
            String s[] = fieldField1.split(",");
            if (s.length > 2) {
            //    data.setField1(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setField1(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField2 = PropertyManager.getVal(name + "_FIELD_FIELD2");
        if (fieldField2 != null && fieldField2.trim().contains(",")) {
            String s[] = fieldField2.split(",");
            if (s.length > 2) {
           //     data.setField2(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setField2(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField3 = PropertyManager.getVal(name + "_FIELD_FIELD3");
        if (fieldField3 != null && fieldField3.trim().contains(",")) {
            String s[] = fieldField3.split(",");
            if (s.length > 2) {
             //   data.setField3(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setField3(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField4 = PropertyManager.getVal(name + "_FIELD_FIELD4");
        if (fieldField4 != null && fieldField4.trim().contains(",")) {
            String s[] = fieldField4.split(",");
            if (s.length > 2) {
           //     data.setField4(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
           //     data.setField4(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField5 = PropertyManager.getVal(name + "_FIELD_FIELD5");
        if (fieldField5 != null && fieldField5.trim().contains(",")) {
            String s[] = fieldField5.split(",");
            if (s.length > 2) {
             //   data.setField5(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setField5(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField6 = PropertyManager.getVal(name + "_FIELD_FIELD6");
        if (fieldField6 != null && fieldField6.trim().contains(",")) {
            String s[] = fieldField6.split(",");
            if (s.length > 2) {
           //     data.setField6(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
           //     data.setField6(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField7 = PropertyManager.getVal(name + "_FIELD_FIELD7");
        if (fieldField7 != null && fieldField7.trim().contains(",")) {
            String s[] = fieldField7.split(",");
            if (s.length > 2) {
           //     data.setField7(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setField7(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField8 = PropertyManager.getVal(name + "_FIELD_FIELD8");
        if (fieldField8 != null && fieldField8.trim().contains(",")) {
            String s[] = fieldField8.split(",");
            if (s.length > 2) {
            //    data.setField8(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
             //   data.setField8(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField9 = PropertyManager.getVal(name + "_FIELD_FIELD9");
        if (fieldField9 != null && fieldField9.trim().contains(",")) {
            String s[] = fieldField9.split(",");
            if (s.length > 2) {
           //     data.setField9(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setField9(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField10 = PropertyManager.getVal(name + "_FIELD_FIELD10");
        if (fieldField10 != null && fieldField10.trim().contains(",")) {
            String s[] = fieldField10.split(",");
            if (s.length > 2) {
            //    data.setField10(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
           //     data.setField10(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField11 = PropertyManager.getVal(name + "_FIELD_FIELD11");
        if (fieldField11 != null && fieldField11.trim().contains(",")) {
            String s[] = fieldField11.split(",");
            if (s.length > 2) {
           //     data.setField11(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
           //     data.setField11(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField12 = PropertyManager.getVal(name + "_FIELD_FIELD12");
        if (fieldField12 != null && fieldField12.trim().contains(",")) {
            String s[] = fieldField12.split(",");
            if (s.length > 2) {
            //    data.setField12(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setField12(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField13 = PropertyManager.getVal(name + "_FIELD_FIELD13");
        if (fieldField13 != null && fieldField13.trim().contains(",")) {
            String s[] = fieldField13.split(",");
            if (s.length > 2) {
            //    data.setField13(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setField13(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField14 = PropertyManager.getVal(name + "_FIELD_FIELD14");
        if (fieldField14 != null && fieldField14.trim().contains(",")) {
            String s[] = fieldField14.split(",");
            if (s.length > 2) {
           //     data.setField14(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
           //     data.setField14(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField15 = PropertyManager.getVal(name + "_FIELD_FIELD15");
        if (fieldField15 != null && fieldField15.trim().contains(",")) {
            String s[] = fieldField15.split(",");
            if (s.length > 2) {
            //    data.setField15(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setField15(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField16 = PropertyManager.getVal(name + "_FIELD_FIELD16");
        if (fieldField16 != null && fieldField16.trim().contains(",")) {
            String s[] = fieldField16.split(",");
            if (s.length > 2) {
           //     data.setField16(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
           //     data.setField16(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField17 = PropertyManager.getVal(name + "_FIELD_FIELD17");
        if (fieldField17 != null && fieldField17.trim().contains(",")) {
            String s[] = fieldField17.split(",");
            if (s.length > 2) {
            //    data.setField17(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setField17(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField18 = PropertyManager.getVal(name + "_FIELD_FIELD18");
        if (fieldField18 != null && fieldField18.trim().contains(",")) {
            String s[] = fieldField18.split(",");
            if (s.length > 2) {
           //     data.setField18(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
           //     data.setField18(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField19 = PropertyManager.getVal(name + "_FIELD_FIELD19");
        if (fieldField19 != null && fieldField19.trim().contains(",")) {
            String s[] = fieldField19.split(",");
            if (s.length > 2) {
           //     data.setField19(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
           //     data.setField19(readField(s[0], objectMap.get(s[1]), null));
            }
        }

        String fieldField20 = PropertyManager.getVal(name + "_FIELD_FIELD20");
        if (fieldField20 != null && fieldField20.trim().contains(",")) {
            String s[] = fieldField20.split(",");
            if (s.length > 2) {
           //     data.setField20(readField(s[0], objectMap.get(s[1]), s[2]));
            } else {
            //    data.setField20(readField(s[0], objectMap.get(s[1]), null));
            }
        }

      //  data.setField5(string(objectMap.values().toArray()));
        return data;

    }

    /**
     * Invoke reflective methods
     *
     * @param methodName
     * @param obj
     * @return
     */
    private String invokeMethod(String methodName, Object obj) {
        if (obj != null
                && (obj.getClass().isPrimitive() || obj instanceof String)) {
            return obj.toString();
        }
        try {
            if (methodName.contains(".")) {
                String level[] = methodName.split("\\.", 2);
                Method m = obj.getClass().getDeclaredMethod(level[0],
                        (Class[]) null);
                m.setAccessible(true);
                Object o = m.invoke(obj, (Object[]) null);
                return invokeMethod(level[1], o);

            } else {
                Method m = obj.getClass().getDeclaredMethod(methodName,
                        (Class[]) null);
                m.setAccessible(true);
                Object o = m.invoke(obj, (Object[]) null);
                if (o != null && o.getClass().isPrimitive()
                        || o instanceof String) {
                    return o.toString();
                }
            }

            return null;
        } catch (InvocationTargetException e) {
            LOGGER.warn("InvocationTargetException: " + methodName);
            return null;
        } catch (IllegalAccessException e) {
            LOGGER.warn("IllegalAccessException: " + methodName);
            return null;
        } catch (NoSuchMethodException e) {
            LOGGER.warn("NoSuchMethodException: " + methodName);
            return null;
        } catch (Exception e) {
            LOGGER
                    .warn("Unknown Exception on method invocation: "
                            + methodName);
            return null;
        }
    }

    /**
     *
     * @param fieldName
     * @param obj
     * @return
     */
    private String readField(String fieldName, Object obj, String methodName) {
        try {
            Field f = obj.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            if (f.getType().isPrimitive()
                    || f.getType().getName().equalsIgnoreCase(
                            "java.lang.String")) {
                return f.get(obj).toString();
            } else {
                if (methodName != null && !methodName.trim().equals("")) {
                    return invokeMethod(methodName, f.get(obj));
                }
            }
            return string(obj);
        } catch (NoSuchFieldException e) {
            LOGGER.warn("NoSuchFieldException: " + fieldName);
            return null;
        } catch (IllegalAccessException e) {
            LOGGER.warn("IllegalAccessException: " + fieldName);
            return null;
        } catch (Exception e) {
            LOGGER.warn("Unknown Exception at reading field: " + methodName);
            return null;
        }
    }

    /**
     * convert object array to string
     *
     * @param obj
     * @return
     */
    public String string(Object[] obj) {
        StringBuffer buffer = new StringBuffer();
        buffer.setLength(CONSTANTS.READ_CAPACITY);
        for (int count = 0; count < obj.length; count++) {
            Field f[] = obj[count].getClass().getDeclaredFields();
            buffer.append(obj[count].getClass().getName() + "\\n");
            for (int i = 0; i < f.length; i++) {
                f[i].setAccessible(true);
                try {
                    if (f[i] != null && f[i].get(obj[count]) != null) {
                        if (f[i].get(obj[count]).toString().length() < 19) {
                            buffer.append("\\t" + f[i].getName() + ":");
                            buffer.append(f[i].get(obj[count]).toString().trim()
                                    + "\\n");
                        }
                    }
                } catch (Exception e) {
                    buffer.append(e.getMessage() + "\\n");
                }
            }
            buffer.append("\\n");
        }
        return buffer.toString();
    }

    /**
     *
     * @param obj
     * @return
     */
    private String string(Object obj) {
        StringBuffer buffer = new StringBuffer();
        buffer.setLength(CONSTANTS.READ_CAPACITY);
        Field f[] = obj.getClass().getDeclaredFields();
        buffer.append(obj.getClass().getName() + "\\n");
        for (int i = 0; i < f.length; i++) {
            f[i].setAccessible(true);
            try {
                if (f[i] != null && f[i].get(obj) != null) {
                    if (f[i].get(obj).toString().length() < 19) {
                        buffer.append("\\t" + f[i].getName() + ":");
                        buffer.append(f[i].get(obj).toString().trim() + "\\n");
                    }
                }
            } catch (Exception e) {
                buffer.append(e.getMessage() + "\\n");
            }
        }
        buffer.append("\\n");
        return buffer.toString();
    }

}
