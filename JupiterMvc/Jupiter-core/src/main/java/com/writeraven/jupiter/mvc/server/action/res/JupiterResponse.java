package com.writeraven.jupiter.mvc.server.action.res;

import com.writeraven.jupiter.mvc.server.action.req.Cookie;

import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/3/5 00:53
 * @since JDK 1.8
 */
public interface JupiterResponse {

    /**
     * get all customer headers
     * @return
     */
    Map<String, String> getHeaders();


    /**
     * set content type
     * @param contentType
     */
    void setContentType(String contentType);

    /**
     * get content type
     * @return
     */
    String getContentType();

    /**
     * set http body
     * @param content
     */
    void setHttpContent(String content);

    /**
     * get http body
     * @return
     */
    String getHttpContent();


    /**
     * set cookie
     * @param cookie cookie
     */
    void setCookie(Cookie cookie) ;


    /**
     * get all cookies
     * @return all cookies
     */
    List<io.netty.handler.codec.http.cookie.Cookie> cookies() ;

}
