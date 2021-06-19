package com.writeraven.jupiter.mvc.server.intercept;

import com.writeraven.jupiter.mvc.server.action.param.Param;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/2/2 15:40
 * @since JDK 1.8
 */
public abstract class InterceptorImpl extends AbstractInterceptor {

    @Override
    public boolean before(JupiterContext context, Param param) {
        return true;
    }

    @Override
    public void after(JupiterContext context,Param param) {

    }
}
