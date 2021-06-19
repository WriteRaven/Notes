package com.writeraven.jupiter.mvc.db.model;

import com.writeraven.jupiter.mvc.db.annotation.FieldName;
import com.writeraven.jupiter.mvc.db.annotation.TableName;
import com.writeraven.jupiter.mvc.db.annotation.PrimaryId;
import lombok.Data;
import lombok.ToString;

/**
 * Function:
 *
 * @author  cyj
 * Date: 2019-11-20 11:26
 * @since JDK 1.8
 */
@Data
@TableName("user")
@ToString
public class User extends Model {
    @PrimaryId
    private Integer id ;
    private String name ;
    private String password ;

    @FieldName(value = "city_id")
    private Integer cityId ;

    private String description ;

}
