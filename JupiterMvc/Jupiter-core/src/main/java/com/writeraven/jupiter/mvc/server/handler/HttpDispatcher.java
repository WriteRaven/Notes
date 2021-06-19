package com.writeraven.jupiter.mvc.server.handler;

import com.writeraven.jupiter.mvc.server.action.param.Param;
import com.writeraven.jupiter.mvc.server.action.param.ParamMap;
import com.writeraven.jupiter.mvc.server.action.req.JupiterHttpRequest;
import com.writeraven.jupiter.mvc.server.action.req.JupiterRequest;
import com.writeraven.jupiter.mvc.server.action.res.JupiterHttpResponse;
import com.writeraven.jupiter.mvc.server.action.res.JupiterResponse;
import com.writeraven.jupiter.mvc.server.bean.BeanManager;
import com.writeraven.jupiter.mvc.server.config.AppConfig;
import com.writeraven.jupiter.mvc.server.constant.JupiterConstant;
import com.writeraven.jupiter.mvc.server.exception.GlobalHandlerException;
import com.writeraven.jupiter.mvc.server.exception.JupiterException;
import com.writeraven.jupiter.mvc.server.intercept.InterceptProcess;
import com.writeraven.jupiter.mvc.server.route.RouteProcess;
import com.writeraven.jupiter.mvc.server.route.RouteScanner;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.extern.slf4j.Slf4j;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2018/8/30 18:47
 * @since JDK 1.8
 */
@Slf4j
@ChannelHandler.Sharable
public final class HttpDispatcher extends SimpleChannelInboundHandler<DefaultHttpRequest> {


    private final AppConfig appConfig = AppConfig.getInstance();
    private final InterceptProcess interceptProcess = InterceptProcess.getInstance();
    private final RouteScanner routeScanner = RouteScanner.getInstance();
    private final RouteProcess routeProcess = RouteProcess.getInstance() ;
    private final BeanManager beanManager = BeanManager.getInstance() ;
    private final GlobalHandlerException exceptionHandle = beanManager.exceptionHandle() ;
    private Exception exception ;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DefaultHttpRequest httpRequest) {

        JupiterRequest jupiterRequest = JupiterHttpRequest.init(httpRequest);
        JupiterResponse jupiterResponse = JupiterHttpResponse.init();

        // set current thread request and response
        JupiterContext.setContext(new JupiterContext(jupiterRequest, jupiterResponse));

        try {
            // request uri
            String uri = jupiterRequest.getUrl();
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(URLDecoder.decode(httpRequest.uri(), "utf-8"));

            // check Root Path
            appConfig.checkRootPath(uri, queryStringDecoder);

            // route Action
            //Class<?> actionClazz = routeAction(queryStringDecoder, appConfig);

            //build paramMap
            Param paramMap = buildParamMap(queryStringDecoder);

            // 责任链-拦截器
            //load interceptors
            interceptProcess.loadInterceptors();

            //interceptor before
            boolean access = interceptProcess.processBefore(paramMap);
            if (!access) {
                return;
            }

            // execute Method
            Method method = routeScanner.routeMethod(queryStringDecoder);
            routeProcess.invoke(method,queryStringDecoder) ;


            //WorkAction action = (WorkAction) actionClazz.newInstance();
            //action.execute(JupiterContext.getContext(), paramMap);


            // interceptor after
            interceptProcess.processAfter(paramMap);

        } catch (Exception e) {
            exceptionCaught(ctx, e);
        } finally {
            // Response
            responseContent(ctx);

            // remove Jupiter thread context
            JupiterContext.removeContext();
        }


    }


    /**
     * Response
     *
     * @param ctx
     */
    private void responseContent(ChannelHandlerContext ctx) {
        JupiterResponse jupiterResponse = JupiterContext.getResponse();
        String context = jupiterResponse.getHttpContent() ;

        ByteBuf buf = Unpooled.wrappedBuffer(context.getBytes(StandardCharsets.UTF_8));
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        buildHeader(response);
        ctx.writeAndFlush(response);
    }

    /**
     * build paramMap
     *
     * @param queryStringDecoder
     * @return
     */
    private Param buildParamMap(QueryStringDecoder queryStringDecoder) {
        Map<String, List<String>> parameters = queryStringDecoder.parameters();
        Param paramMap = new ParamMap();
        for (Map.Entry<String, List<String>> stringListEntry : parameters.entrySet()) {
            String key = stringListEntry.getKey();
            List<String> value = stringListEntry.getValue();
            paramMap.put(key, value.get(0));
        }
        return paramMap;
    }




    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        exception = (Exception) cause;
        if (JupiterException.isResetByPeer(cause.getMessage())){
            return;
        }

        if (exceptionHandle != null){
            exceptionHandle.resolveException(JupiterContext.getContext(),exception);
        }
    }

    /**
     * build Header
     *
     * @param response
     */
    private void buildHeader(DefaultFullHttpResponse response) {
        JupiterResponse jupiterResponse = JupiterContext.getResponse();

        HttpHeaders headers = response.headers();
        headers.setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        headers.set(HttpHeaderNames.CONTENT_TYPE, jupiterResponse.getContentType());

        List<Cookie> cookies = jupiterResponse.cookies();
        for (Cookie cookie : cookies) {
            headers.add(JupiterConstant.ContentType.SET_COOKIE, io.netty.handler.codec.http.cookie.ServerCookieEncoder.LAX.encode(cookie));
        }

    }
}
