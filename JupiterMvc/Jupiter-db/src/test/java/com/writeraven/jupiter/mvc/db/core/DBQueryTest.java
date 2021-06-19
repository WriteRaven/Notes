package com.writeraven.jupiter.mvc.db.core;

import com.writeraven.jupiter.mvc.db.sqlsession.SqlSession;
import com.writeraven.jupiter.mvc.db.sql.EqualToCondition;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import com.writeraven.jupiter.mvc.db.model.User;

import java.util.List;

@Slf4j
public class DBQueryTest {

    @Test
    public void query(){
        SqlSession.init("root","root","jdbc:mysql://localhost:3306/ssm?charset=utf8mb4");
        List<User> all = new DBQuery<User>(User.class).all();
        for (User user : all) {
            log.info(user.toString());
        }
    }
    @Test
    public void query2(){
        SqlSession.init("root","root","jdbc:mysql://localhost:3306/ssm?charset=utf8mb4");
        List<User> all = new DBQuery<User>(User.class).addCondition(new EqualToCondition("password","abc123"))
                .addCondition(new EqualToCondition("id",1)).all();
        for (User user : all) {
            log.info(user.toString());
        }
    }

}