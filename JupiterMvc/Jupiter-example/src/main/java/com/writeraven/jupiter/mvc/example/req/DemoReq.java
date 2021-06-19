package com.writeraven.jupiter.mvc.example.req;

import com.writeraven.jupiter.mvc.db.annotation.FieldName;
import com.writeraven.jupiter.mvc.server.action.req.WorkReq;
import lombok.Data;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2018/8/31 18:59
 * @since JDK 1.8
 */
@Data
public class DemoReq extends WorkReq {

    private Integer id ;

    private String name ;

    private String password;

    private Integer cityId ;

    private String description ;
}
