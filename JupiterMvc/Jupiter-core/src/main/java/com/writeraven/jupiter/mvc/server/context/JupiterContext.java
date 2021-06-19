package com.writeraven.jupiter.mvc.server.context;

import com.alibaba.fastjson.JSON;
import com.writeraven.jupiter.mvc.server.action.req.JupiterRequest;
import com.writeraven.jupiter.mvc.server.action.res.JupiterResponse;
import com.writeraven.jupiter.mvc.server.action.res.WorkRes;
import com.writeraven.jupiter.mvc.server.constant.JupiterConstant;
import com.writeraven.jupiter.mvc.server.thread.ThreadLocalHolder;

/**
 * Function: Jupiter context
 *
 * @author  cyj
 *         Date: 2021/3/5 00:23
 * @since JDK 1.8
 */
public final class JupiterContext {


    /**
     * current thread request
     */
    private JupiterRequest request ;

    /**
     * current thread response
     */
    private JupiterResponse response ;

    public JupiterContext(JupiterRequest request, JupiterResponse response) {
        this.request = request;
        this.response = response;
    }


    /**
     * response json message
     * @param workRes
     */
    public void json(WorkRes workRes){
        JupiterContext.getResponse().setContentType(JupiterConstant.ContentType.JSON);
        JupiterContext.getResponse().setHttpContent(JSON.toJSONString(workRes));
    }

    /**
     * response text message
     * @param text response body
     */
    public void text(String text){
        JupiterContext.getResponse().setContentType(JupiterConstant.ContentType.TEXT);
        JupiterContext.getResponse().setHttpContent(text);
    }

    /**
     * response html
     * @param html response body
     */
    public void html(String html){
        JupiterContext.getResponse().setContentType(JupiterConstant.ContentType.HTML);
        JupiterContext.getResponse().setHttpContent(html);
    }

    public static JupiterRequest getRequest(){
        return JupiterContext.getContext().request ;
    }

    public JupiterRequest request(){
        return JupiterContext.getContext().request ;
    }

    public static JupiterResponse getResponse(){
        return JupiterContext.getContext().response ;
    }

    public static void setContext(JupiterContext context){
        ThreadLocalHolder.setJupiterContext(context) ;
    }


    public static void removeContext(){
        ThreadLocalHolder.removeJupiterContext();
    }

    public static JupiterContext getContext(){
        return ThreadLocalHolder.getJupiterContext() ;
    }
}
