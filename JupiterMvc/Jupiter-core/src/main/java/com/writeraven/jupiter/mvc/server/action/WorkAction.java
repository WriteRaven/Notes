package com.writeraven.jupiter.mvc.server.action;

import com.writeraven.jupiter.mvc.server.action.param.Param;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;

/**
 * @class: cicada
 * @description:
 * @author: cyj
 * @create: 2021-04-28 11:13
 **/
@Deprecated
public interface WorkAction {

    /**
     * abstract execute method
     * @param context current context
     * @param param request params
     * @throws Exception throw exception
     */
    void execute(JupiterContext context , Param param) throws Exception;
}
