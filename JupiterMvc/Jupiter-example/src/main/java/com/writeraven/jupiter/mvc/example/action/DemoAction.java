package com.writeraven.jupiter.mvc.example.action;

import com.writeraven.jupiter.mvc.example.configuration.KafkaConfiguration;
import com.writeraven.jupiter.mvc.example.configuration.RedisConfiguration;
import com.writeraven.jupiter.mvc.example.enums.StatusEnum;
import com.writeraven.jupiter.mvc.example.res.DemoResVO;
import com.writeraven.jupiter.mvc.server.action.WorkAction;
import com.writeraven.jupiter.mvc.server.action.param.Param;
import com.writeraven.jupiter.mvc.server.action.res.WorkRes;
import com.writeraven.jupiter.mvc.server.annotation.Action;
import com.writeraven.jupiter.mvc.server.configuration.ApplicationConfiguration;
import com.writeraven.jupiter.mvc.server.configuration.ConfigurationHolder;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2018/8/31 18:52
 * @since JDK 1.8
 */
@Action(value = "demoAction")
@Deprecated
@Slf4j
public class DemoAction implements WorkAction {



    private static AtomicLong index = new AtomicLong();

    @Override
    public void execute(JupiterContext context, Param paramMap) throws Exception {

        KafkaConfiguration configuration = (KafkaConfiguration) ConfigurationHolder.getConfiguration(KafkaConfiguration.class);
        RedisConfiguration redisConfiguration = (RedisConfiguration) ConfigurationHolder.getConfiguration(RedisConfiguration.class);
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) ConfigurationHolder.getConfiguration(ApplicationConfiguration.class);

        String brokerList = configuration.get("kafka.broker.list");
        String redisHost = redisConfiguration.get("redis.host");
        String port = applicationConfiguration.get("Jupiter.port");

        log.info("Configuration brokerList=[{}],redisHost=[{}] port=[{}]", brokerList, redisHost, port);

        String name = paramMap.getString("name");
        Integer id = paramMap.getInteger("id");
        log.info("name=[{}],id=[{}]", name, id);


        String url = context.request().getUrl();
        String method = context.request().getMethod();

        DemoResVO demoResVO = new DemoResVO();
        demoResVO.setIndex(index.incrementAndGet());
        demoResVO.setMsg(url + " " + method);
        WorkRes<DemoResVO> res = new WorkRes();
        res.setCode(StatusEnum.SUCCESS.getCode());
        res.setMessage(StatusEnum.SUCCESS.getMessage());
        res.setDataBody(demoResVO);

        context.json(res);
    }

}
