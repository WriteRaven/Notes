package com.writeraven.jupiter.mvc.server.action.req;

import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import com.writeraven.jupiter.mvc.server.constant.JupiterConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * Function:
 *
 * @author  cyj
 * Date: 2021/3/5 00:42
 * @since JDK 1.8
 */
public class JupiterHttpRequest implements JupiterRequest {

    private String method;

    private String url;

    private String clientAddress;

    private Map<String, Cookie> cookie = new HashMap<>(8);
    private Map<String, String> headers = new HashMap<>(8);

    private JupiterHttpRequest() {
    }

    public static JupiterHttpRequest init(DefaultHttpRequest httpRequest) {
        JupiterHttpRequest request = new JupiterHttpRequest();
        request.method = httpRequest.method().name();
        request.url = httpRequest.uri();

        //build headers
        buildHeaders(httpRequest, request);

        //initBean cookies
        initCookies(request);

        return request;
    }

    /**
     * build headers
     *
     * @param httpRequest io.netty.httprequest
     * @param request     Jupiter request
     */
    private static void buildHeaders(DefaultHttpRequest httpRequest, JupiterHttpRequest request) {
        for (Map.Entry<String, String> entry : httpRequest.headers().entries()) {
            request.headers.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * initBean cookies
     *
     * @param request request
     */
    private static void initCookies(JupiterHttpRequest request) {
        for (Map.Entry<String, String> entry : request.headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!key.equals(JupiterConstant.ContentType.COOKIE)) {
                continue;
            }

            for (io.netty.handler.codec.http.cookie.Cookie cookie : ServerCookieDecoder.LAX.decode(value)) {
                Cookie JupiterCookie = new Cookie();
                JupiterCookie.setName(cookie.name());
                JupiterCookie.setValue(cookie.value());
                JupiterCookie.setDomain(cookie.domain());
                JupiterCookie.setMaxAge(cookie.maxAge());
                JupiterCookie.setPath(cookie.path());
                request.cookie.put(JupiterCookie.getName(), JupiterCookie);
            }
        }
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public Cookie getCookie(String key) {
        return cookie.get(key);
    }
}
