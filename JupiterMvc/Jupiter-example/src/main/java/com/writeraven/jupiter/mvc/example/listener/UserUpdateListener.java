package com.writeraven.jupiter.mvc.example.listener;

import com.writeraven.jupiter.mvc.db.listener.DataChangeListener;
import lombok.extern.slf4j.Slf4j;
/**
 * Function:
 *
 * @author  cyj
 * Date: 2020-03-29 01:33
 * @since JDK 1.8
 */
@Slf4j
public class UserUpdateListener implements DataChangeListener {
    @Override
    public void listener(Object obj) {
        log.info("user update data={}", obj.toString());
    }
}
