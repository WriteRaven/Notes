package com.writeraven.jupiter.mvc.server.intercept;

import com.writeraven.jupiter.mvc.server.action.param.Param;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;
import lombok.Getter;
import lombok.Setter;

/**
 * Function: common interceptor
 *      拦截器
 * @author  cyj
 *         Date: 2021/2/2 14:39
 * @since JDK 1.8
 */
@Setter
@Getter
public abstract class AbstractInterceptor {


    private int order ;

    /**
     * before
     * @param context
     * @param param
     * @return
     * true if the execution chain should proceed with the next interceptor or the handler itself
     * @throws Exception
     */
    protected boolean before(JupiterContext context,Param param) throws Exception{
        return true;
    }


    /**
     * after
     * @param context
     * @param param
     * @throws Exception
     */
    protected void after(JupiterContext context,Param param) throws Exception{}
}
