package com.writeraven.jupiter.mvc.server.action.req;

import lombok.Getter;
import lombok.Setter;

/**
 * @author  cyj
 */
@Setter
@Getter
public class WorkReq {

    private Integer timeStamp;

    @Override
    public String toString() {
        return "WorkReq{" +
                "timeStamp=" + timeStamp +
                '}';
    }
}
