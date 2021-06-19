package com.writeraven.jupiter.mvc.server.action.res;

import com.writeraven.jupiter.mvc.server.action.req.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import com.writeraven.jupiter.mvc.server.constant.JupiterConstant;
import com.writeraven.jupiter.mvc.server.exception.JupiterException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @author  cyj
 * Date: 2021/3/5 01:13
 * @since JDK 1.8
 */
@Setter
@Getter
public class JupiterHttpResponse implements JupiterResponse {

    private Map<String, String> headers = new HashMap<>(8);

    private String contentType;

    private String httpContent;

    private List<io.netty.handler.codec.http.cookie.Cookie> cookies = new ArrayList<>(6);

    private JupiterHttpResponse() {
    }

    public static JupiterHttpResponse init() {
        JupiterHttpResponse response = new JupiterHttpResponse();
        response.contentType = JupiterConstant.ContentType.TEXT;
        return response;
    }

    @Override
    public String getHttpContent() {
        return this.httpContent == null ? "" : this.httpContent;
    }

    public void setHeaders(String key, String value) {
        this.headers.put(key, value);
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public void setCookie(Cookie JupiterCookie) {
        if (null == JupiterCookie) {
            throw new JupiterException("cookie is null!");
        }

        if (null == JupiterCookie.getName()) {
            throw new JupiterException("cookie.getName() is null!");
        }
        if (null == JupiterCookie.getValue()) {
            throw new JupiterException("cookie.getValue() is null!");
        }

        DefaultCookie cookie = new DefaultCookie(JupiterCookie.getName(), JupiterCookie.getValue());

        cookie.setPath("/");
        cookie.setMaxAge(JupiterCookie.getMaxAge());
        cookies.add(cookie);
    }

    @Override
    public List<io.netty.handler.codec.http.cookie.Cookie> cookies() {
        return cookies;
    }


}
