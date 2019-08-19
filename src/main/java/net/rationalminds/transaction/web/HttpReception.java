package net.rationalminds.transaction.web;

import java.util.List;

import org.aspectj.lang.JoinPoint;

import net.rationalminds.dto.EndpointModel;
import net.rationalminds.transaction.guid.GUIDSelectionFactory;
import net.rationalminds.transfer.ReflectionUtils;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;

public class HttpReception {

    private static boolean toProbe = false;

    static {
        List<String> modes = PropertyManager.CORRELATION_MODES;
        toProbe = modes.contains(CONSTANTS.TECH.JAX_RPC);
        toProbe = toProbe || modes.contains(CONSTANTS.TECH.JAX_WS_JAVA6);
        toProbe = toProbe || modes.contains(CONSTANTS.TECH.APACHE);
    }

    public void recieve(JoinPoint jp){
    	Object[] allArgs = jp.getArgs();
		String uid = null;
		if(allArgs != null && allArgs.length > 0 && toProbe )
		{
			//look for HttpMessage object
			for(int i =0;i<allArgs.length;i++)
			{
				if(ReflectionUtils.instanceOf("javax.servlet.http.HttpServletRequest", allArgs[i]) && allArgs[i] !=null)
				{
					Object request = allArgs[i];
					uid = ReflectionUtils.invokeMethod("getHeader", request, new Object[]{CONSTANTS.GUID});
					break;
				}
			}
		}
		if (uid != null) {
			GUIDSelectionFactory.selectMedia(uid);
		}
    }
    
    public void recieve(Object obj) {
        String uid = null;

        if (obj !=null && toProbe) {
               uid = ReflectionUtils.invokeMethod("getHeader", obj, new Object[]{CONSTANTS.GUID});
        }
        if (uid != null) {
			GUIDSelectionFactory.selectMedia(uid);
		}
    }

}
