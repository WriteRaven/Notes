package com.writeraven.jupiter.mvc.server.action.res;

import lombok.Getter;
import lombok.Setter;

/**
 * Function:
 *      承接从数据库返回的数据
 * @author  cyj
 *         Date: 2018/8/31 16:03
 * @since JDK 1.8
 */
@Setter
@Getter
public class WorkRes<T> {
    private String code;

    private String message;

    private T dataBody ;

}
