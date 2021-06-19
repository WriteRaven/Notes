package com.writeraven.jupiter.mvc.example.exception;

import com.writeraven.jupiter.mvc.server.action.res.WorkRes;
import com.writeraven.jupiter.mvc.server.annotation.Bean;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;
import com.writeraven.jupiter.mvc.server.exception.GlobalHandlerException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
/**
 * Function:
 *
 * @author  cyj
 * Date: 2019-07-11 12:07
 * @since JDK 1.8
 */

@Bean
@Slf4j
public class ExceptionHandle implements GlobalHandlerException {

    @Override
    public void resolveException(JupiterContext context, Exception e) {
        log.error("Exception", e);
        WorkRes workRes = new WorkRes();
        workRes.setCode("500");
        workRes.setMessage(e.getClass().getName() + "系统运行出现异常");
        context.json(workRes);
    }
}
