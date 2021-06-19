package com.writeraven.jupiter.mvc.db.core.handle;

/**
 * Function:
 *
 * @author  cyj
 * Date: 2019-12-03 23:35
 * @since JDK 1.8
 */
public interface AbstractDBHandler {

    /** update model
     * @param obj model of db entity
     * @return
     */
    int update(Object obj) ;


    /**
     * insert model
     * @param obj
     * @return
     */
    void insert(Object obj) ;

    void delete(Object obj);

}
