package com.writeraven.jupiter.mvc.example.init;

import com.writeraven.jupiter.mvc.db.sqlsession.SqlSession;
import com.writeraven.jupiter.mvc.server.bootstrap.InitializeHandler;
import com.writeraven.jupiter.mvc.server.configuration.ApplicationConfiguration;
import com.writeraven.jupiter.mvc.server.configuration.ConfigurationHolder;
import lombok.extern.slf4j.Slf4j;
/**
 * Function:
 *
 * @author  cyj
 * Date: 2020-02-18 01:40
 * @since JDK 1.8
 */
@Slf4j
public class DBInit extends InitializeHandler {

    @Override
    public void handle() throws Exception {
        ApplicationConfiguration configuration = (ApplicationConfiguration) ConfigurationHolder.getConfiguration(ApplicationConfiguration.class);
        String username = configuration.get("db.username");
        String pwd = configuration.get("db.pwd");
        String url = configuration.get("db.url");
        SqlSession.init(username, pwd, url);
        log.info("db init success!!");
    }
}
