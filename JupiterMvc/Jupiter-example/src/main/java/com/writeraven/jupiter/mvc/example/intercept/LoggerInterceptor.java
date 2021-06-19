package com.writeraven.jupiter.mvc.example.intercept;

import com.writeraven.jupiter.mvc.server.action.param.Param;
import com.writeraven.jupiter.mvc.server.annotation.Interceptor;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;
import com.writeraven.jupiter.mvc.server.intercept.AbstractInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * Function: common interceptor
 *
 * @author  cyj
 *         Date: 2021/2/2 14:39
 * @since JDK 1.8
 */
@Interceptor(order = 1)
@Slf4j
public class LoggerInterceptor extends AbstractInterceptor {


    @Override
    public boolean before(JupiterContext context, Param param) throws Exception {
        return super.before(context, param);
    }

    @Override
    public void after(JupiterContext context, Param param) {
        log.info("logger param=[{}]",param.toString());
    }
}
