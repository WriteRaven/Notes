package com.writeraven.jupiter.mvc.server.action.req;

import lombok.Getter;
import lombok.Setter;

/**
 * Function: cookie
 *
 * @author  cyj
 *         Date: 2018/12/4 18:56
 * @since JDK 1.8
 */
@Getter
@Setter
public class Cookie {

    private String name ;
    private String value ;
    private String path ;
    private String domain ;
    private long maxAge = 1000000L;

    @Override
    public String toString() {
        return "Cookie{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", path='" + path + '\'' +
                ", domain='" + domain + '\'' +
                ", maxAge=" + maxAge +
                '}';
    }
}
