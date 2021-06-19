package com.writeraven.jupiter.mvc.server.action.req;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/3/5 00:40
 * @since JDK 1.8
 */
public interface JupiterRequest {

    /**
     * get request method
     * @return
     */
    String getMethod() ;

    /**
     * get request url
     * @return
     */
    String getUrl() ;

    /**
     * get cookie by key
     * @param key
     * @return return cookie by key
     */
    Cookie getCookie(String key) ;


}
