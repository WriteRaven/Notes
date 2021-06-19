package com.writeraven.jupiter.mvc.example.action;

import com.writeraven.jupiter.mvc.db.core.DBQuery;
import com.writeraven.jupiter.mvc.db.core.handle.AbstractDBHandler;
import com.writeraven.jupiter.mvc.db.core.handle.HandlerProxy;
import com.writeraven.jupiter.mvc.db.sql.EqualToCondition;
import com.writeraven.jupiter.mvc.example.exception.ExceptionHandle;
import com.writeraven.jupiter.mvc.example.model.User;
import com.writeraven.jupiter.mvc.example.req.DemoReq;
import com.writeraven.jupiter.mvc.server.action.req.Cookie;
import com.writeraven.jupiter.mvc.server.action.res.WorkRes;
import com.writeraven.jupiter.mvc.server.annotation.Action;
import com.writeraven.jupiter.mvc.server.annotation.Route;
import com.writeraven.jupiter.mvc.server.bean.BeanManager;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;
import lombok.extern.slf4j.Slf4j;
import com.writeraven.jupiter.mvc.example.listener.UserSaveListener;
import com.writeraven.jupiter.mvc.example.listener.UserUpdateListener;

import java.util.List;

/**
 * Function:
 *
 * @author  cyj
 * Date: 2018/11/13 01:12
 * @since JDK 1.8
 */
@Action("routeAction")
@Slf4j
public class RouteAction {


    @Route("getUser")
    public void getUser(DemoReq req) {
        log.info(req.toString());
        WorkRes<List> reqWorkRes = new WorkRes<>();

        List all = new DBQuery<User>(User.class)
                .addCondition(new EqualToCondition("password", req.getPassword()))
                .addCondition(new EqualToCondition("name", req.getName())).all();

        reqWorkRes.setDataBody(all);
        JupiterContext.getContext().json(reqWorkRes);
    }

    @Route("getUserById")
    public void getUserById(DemoReq req) {
        log.info(req.toString());
        WorkRes<List> reqWorkRes = new WorkRes<>();

        List all = new DBQuery<User>(User.class)
                .addCondition(new EqualToCondition("password", req.getPassword()))
                .addCondition(new EqualToCondition("id", req.getId())).all();

        reqWorkRes.setDataBody(all);
        JupiterContext.getContext().json(reqWorkRes);
    }


    @Route("saveUser")
    public void saveUser(DemoReq req) {
        AbstractDBHandler handle = (AbstractDBHandler) new HandlerProxy(AbstractDBHandler.class).getInstance(new UserSaveListener());
        User user = new User();
        user.setName(req.getName());
        user.setPassword(req.getPassword());
        user.setCityId(req.getCityId());
        user.setDescription(req.getDescription());

        handle.insert(user);
        WorkRes workRes = new WorkRes();
        workRes.setCode("200");
        workRes.setMessage("success");
        JupiterContext.getContext().json(workRes);
    }

    @Route("updateUser")
    public void updateUser(DemoReq req) {
        AbstractDBHandler handle = (AbstractDBHandler) new HandlerProxy(AbstractDBHandler.class).getInstance(new UserUpdateListener());
        User user = new User();
        user.setId(req.getId());
        user.setName(req.getName());
        user.setPassword(req.getPassword());
        user.setCityId(req.getCityId());
        user.setDescription(req.getDescription());

        int count = handle.update(user);

        WorkRes workRes = new WorkRes();
        workRes.setCode("200");
        workRes.setMessage("success");
        workRes.setDataBody(count);
        JupiterContext.getContext().json(workRes);
    }

    @Route("getUserText")
    public void getUserText(DemoReq req) {

        log.info(req.toString());
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>();
        reqWorkRes.setMessage("hello =" + req.getName());
        Cookie cookie = new Cookie();
        cookie.setName("cookie");
        cookie.setValue(req.getName());
        JupiterContext.getResponse().setCookie(cookie);
        JupiterContext.getContext().text(req.toString());
    }

    @Route("getInfo")
    public void getInfo(DemoReq req) {

        Cookie cookie = JupiterContext.getRequest().getCookie("cookie");
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>();
        reqWorkRes.setMessage("getInfo =" + req.toString() + "cookie=" + cookie.toString());
        JupiterContext.getContext().json(reqWorkRes);
    }

    @Route("getReq")
    public void getReq(JupiterContext context, DemoReq req) {
        WorkRes<DemoReq> reqWorkRes = new WorkRes<>();
        reqWorkRes.setMessage("getReq =" + req.toString());
        context.json(reqWorkRes);
    }


    @Route("test")
    public void test(JupiterContext context) {
        ExceptionHandle bean = BeanManager.getInstance().getBean(ExceptionHandle.class);
        log.info("====" + bean.getClass());
        context.html("<p>12345</p>");
    }

}
