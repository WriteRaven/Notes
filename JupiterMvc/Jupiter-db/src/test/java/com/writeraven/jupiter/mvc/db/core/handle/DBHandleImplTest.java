package com.writeraven.jupiter.mvc.db.core.handle;

import com.writeraven.jupiter.mvc.db.sqlsession.SqlSession;
import com.writeraven.jupiter.mvc.db.listener.DataChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import com.writeraven.jupiter.mvc.db.model.User;

@Slf4j
public class DBHandleImplTest {

    @Test
    public void update2(){
        SqlSession.init("root","root","jdbc:mysql://localhost:3306/ssm?charset=utf8mb4&useUnicode=true&characterEncoding=utf-8");
        User user = new User();
        user.setId(1);
        user.setName("abc");
        AbstractDBHandler handle = (AbstractDBHandler) new HandlerProxy(AbstractDBHandler.class).getInstance(new DataChangeListener() {
            @Override
            public void listener(Object obj) {
                User user2 = (User) obj;
                log.info("call back " + user2.toString());
            }
        });
        int x = handle.update(user) ;
        System.out.println(x);
    }


    @Test
    public void insert(){
        SqlSession.init("root","root","jdbc:mysql://localhost:3306/ssm?charset=utf8mb4&useUnicode=true&characterEncoding=utf-8");
        User user = new User();
        user.setName("abc");
        user.setDescription("哈哈哈");
        AbstractDBHandler handle = (AbstractDBHandler) new HandlerProxy(AbstractDBHandler.class).getInstance() ;
        handle.insert(user) ;
    }
}