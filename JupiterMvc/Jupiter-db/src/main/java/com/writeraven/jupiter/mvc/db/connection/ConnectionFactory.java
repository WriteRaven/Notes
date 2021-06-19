package com.writeraven.jupiter.mvc.db.connection;

import com.writeraven.jupiter.mvc.db.sqlsession.SqlSession;

import java.sql.Connection;

/**
 * Function:
 *
 * @author  cyj
 * Date: 2020-02-28 00:35
 * @since JDK 1.8
 */
public interface ConnectionFactory {

    /**
     * get db connection
     * @param sqlSession
     * @return
     */
    Connection getConnection(SqlSession sqlSession) ;
}
