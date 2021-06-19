package com.writeraven.jupiter.mvc.example.action;


import com.writeraven.jupiter.mvc.server.annotation.Action;
import com.writeraven.jupiter.mvc.server.annotation.Route;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/3/5 02:34
 * @since JDK 1.8
 */
@Action("textAction")
public class TextAction  {


    @Route("hello")
    public void hello() throws Exception {
        JupiterContext context = JupiterContext.getContext();

        String url = context.request().getUrl();
        String method = context.request().getMethod();
        context.text("hello world url=" + url + " method=" + method);
    }

    @Route("hello2")
    public void hello2(JupiterContext context) throws Exception {

        String url = context.request().getUrl();
        String method = context.request().getMethod();
        context.text("hello world2 url=" + url + " method=" + method);
    }
}
