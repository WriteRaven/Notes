package com.writeraven.jupiter.mvc.server.bootstrap;

/**
 * initialize something about db/redis/kafka etc.
 */
public abstract class InitializeHandler {

    /**
     *
     * @throws Exception
     */
    public abstract void handle() throws Exception;
}
