package com.writeraven.jupiter.mvc.server.thread;

import io.netty.util.concurrent.FastThreadLocal;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/2/10 19:50
 * @since JDK 1.8
 */
public class ThreadLocalHolder {

    private static final FastThreadLocal<Long> LOCAL_TIME= new FastThreadLocal() ;

    private static final FastThreadLocal<JupiterContext> Jupiter_CONTEXT= new FastThreadLocal() ;


    /**
     * set Jupiter context
     * @param context current context
     */
    public static void setJupiterContext(JupiterContext context){
        Jupiter_CONTEXT.set(context) ;
    }

    /**
     * remove Jupiter context
     */
    public static void removeJupiterContext(){
        Jupiter_CONTEXT.remove();
    }

    /**
     * @return get Jupiter context
     */
    public static JupiterContext getJupiterContext(){
        return Jupiter_CONTEXT.get() ;
    }

    /**
     * Set time
     * @param time current time
     */
    public static void setLocalTime(long time){
        LOCAL_TIME.set(time) ;
    }

    /**
     * Get time and remove value
     * @return get local time
     */
    public static Long getLocalTime(){
        Long time = LOCAL_TIME.get();
        LOCAL_TIME.remove();
        return time;
    }

}
