package com.writeraven.jupiter.mvc.server.exception;

import com.writeraven.jupiter.mvc.server.context.JupiterContext;

/**
 * @class: cicada
 * @description:
 * @author: cyj
 * @create: 2021-04-25 16:12
 **/
public interface GlobalHandlerException {
    /**
     * exception handle
     * @param context
     * @param e
     */
    void resolveException(JupiterContext context, Exception e) ;

}
