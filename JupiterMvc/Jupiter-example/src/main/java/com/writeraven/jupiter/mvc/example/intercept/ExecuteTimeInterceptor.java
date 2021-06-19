package com.writeraven.jupiter.mvc.example.intercept;

import com.writeraven.jupiter.mvc.server.action.param.Param;
import com.writeraven.jupiter.mvc.server.annotation.Interceptor;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;
import com.writeraven.jupiter.mvc.server.intercept.AbstractInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/2/2 15:21
 * @since JDK 1.8
 */
@Interceptor(order = 1)
@Slf4j
public class ExecuteTimeInterceptor extends AbstractInterceptor {


    private Long start;

    private Long end;

    @Override
    public boolean before(JupiterContext context, Param param) {
        start = System.currentTimeMillis();
        log.info("拦截请求");
        return true;
    }

    @Override
    public void after(JupiterContext context,Param param) {
        end = System.currentTimeMillis();

        log.info("cast [{}] times", end - start);
    }
}
