package com.writeraven.jupiter.mvc.server.bootstrap;

import com.writeraven.jupiter.mvc.server.configuration.ApplicationConfiguration;
import com.writeraven.jupiter.mvc.server.configuration.ConfigurationHolder;
import com.writeraven.jupiter.mvc.server.constant.JupiterConstant;
import com.writeraven.jupiter.mvc.server.init.JupiterInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import com.writeraven.jupiter.mvc.server.bean.BeanManager;
import com.writeraven.jupiter.mvc.server.config.AppConfig;
import com.writeraven.jupiter.mvc.server.context.JupiterContext;
import com.writeraven.jupiter.mvc.server.thread.ThreadLocalHolder;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/2/10 21:56
 * @since JDK 1.8
 */
@Slf4j
public class BootStrap {

    private static AppConfig appConfig = AppConfig.getInstance() ;
    private static EventLoopGroup boss = new NioEventLoopGroup(1,new DefaultThreadFactory("boss"));
    private static EventLoopGroup work = new NioEventLoopGroup(0,new DefaultThreadFactory(JupiterConstant.SystemProperties.APPLICATION_THREAD_WORK_NAME));
    private static Channel channel ;

    /**
     * Start netty Server
     *
     * @throws Exception
     */
    public static void startJupiter() throws Exception {
        // start
        startServer();

        // register shutdown hook
        shutDownServer();

        // synchronized channel
        joinServer();
    }

    /**
     * start netty server
     * @throws InterruptedException
     */
    private static void startServer() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new JupiterInitializer());

        ChannelFuture future = bootstrap.bind(AppConfig.getInstance().getPort()).sync();
        if (future.isSuccess()) {
            appLog();
        }
        channel = future.channel();
    }

    private static void joinServer() throws Exception {
        channel.closeFuture().sync();
    }

    private static void appLog() {
        Long start = ThreadLocalHolder.getLocalTime();
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) ConfigurationHolder.getConfiguration(ApplicationConfiguration.class);
        long end = System.currentTimeMillis();
        log.info("Jupiter started on port: {}.cost {}ms", applicationConfiguration.get(JupiterConstant.Jupiter_PORT), end - start);
        log.info(">> access http://{}:{}{} <<","127.0.0.1",appConfig.getPort(),appConfig.getRootPath());
    }

    /**
     * shutdown server
     */
    private static void shutDownServer() {
        ShutDownThread shutDownThread = new ShutDownThread();
        shutDownThread.setName(JupiterConstant.SystemProperties.APPLICATION_THREAD_SHUTDOWN_NAME);
        Runtime.getRuntime().addShutdownHook(shutDownThread);
    }

    private static class ShutDownThread extends Thread {
        @Override
        public void run() {
            log.info("Jupiter server stop...");
            JupiterContext.removeContext();

            BeanManager.getInstance().releaseBean();

            boss.shutdownGracefully();
            work.shutdownGracefully();

            log.info("Jupiter server has been successfully stopped.");
        }

    }
}
