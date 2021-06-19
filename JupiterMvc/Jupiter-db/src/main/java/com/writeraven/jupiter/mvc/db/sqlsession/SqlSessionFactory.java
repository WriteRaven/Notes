package com.writeraven.jupiter.mvc.db.sqlsession;

/**
 * Function:
 *
 * @author  cyj
 * Date: 2019-12-23 15:41
 * @since JDK 1.8
 */
public abstract class SqlSessionFactory {

    private SqlSession sqlSession;

    public SqlSessionFactory() {
        sqlSession = SqlSession.getInstance();
    }

    public SqlSession getSession(){
        return this.sqlSession ;
    }
}
