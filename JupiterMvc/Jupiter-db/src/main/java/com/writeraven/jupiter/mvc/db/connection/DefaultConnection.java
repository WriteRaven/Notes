package com.writeraven.jupiter.mvc.db.connection;

import com.writeraven.jupiter.mvc.db.sqlsession.SqlSession;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Function:
 *
 * @author  cyj
 * Date: 2020-02-28 00:37
 * @since JDK 1.8
 */
@Slf4j
public class DefaultConnection implements ConnectionFactory {

    private Connection connection;

    @Override
    public Connection getConnection(SqlSession sqlSession) {
        if (connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(sqlSession.getUrl(), sqlSession.getUserName(), sqlSession.getPwd());
            } catch (Exception e) {
               log.error("Exception", e);
            }
        }
        return connection;
    }
}
