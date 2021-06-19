package com.writeraven.jupiter.mvc.db.connection;

import com.writeraven.jupiter.mvc.db.sqlsession.SqlSession;

import java.sql.Connection;

/**
 * Function:
 *
 * @author  cyj
 * Date: 2020-02-28 00:42
 * @since JDK 1.8
 */
public class ConnectionPool implements ConnectionFactory {
    @Override
    public Connection getConnection(SqlSession sqlSession) {
        return null;
    }
}
