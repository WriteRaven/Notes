package com.writeraven.jupiter.mvc.example;

import com.writeraven.jupiter.mvc.server.JupiterServer;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2018/8/31 16:27
 * @since JDK 1.8
 */
public class MainStart {

    public static void main(String[] args) throws Exception {
        JupiterServer.start(MainStart.class,"/jupiter-example") ;
    }
}
