package com.writeraven.jupiter.mvc.server.config;

import com.writeraven.jupiter.mvc.server.enums.StatusEnum;
import com.writeraven.jupiter.mvc.server.exception.JupiterException;
import com.writeraven.jupiter.mvc.server.util.PathUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Function:
 *
 * @author  cyj
 * Date: 2021/2/1 14:00
 * @since JDK 1.8
 */
@Setter
@Getter
public final class AppConfig {

    private String rootPackageName;

    private String rootPath;

    private Integer port = 7317;

    /**
     * simple singleton Object
     */
    private static volatile AppConfig config;

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        if (config == null) {
            synchronized (AppConfig.class) {
                if (Objects.isNull(config)) {
                    config = new AppConfig();
                }
            }
        }
        return config;
    }

    public void setRootPackageName(Class<?> clazz) {
        if (clazz.getPackage() == null) {
            throw new JupiterException(StatusEnum.NULL_PACKAGE, "[" + clazz.getName() + ".java]:" + StatusEnum.NULL_PACKAGE.getMessage());
        }
        this.rootPackageName = clazz.getPackage().getName();
    }

    /**
     * check Root Path
     *
     * @param uri
     * @param queryStringDecoder
     * @return
     */
    public void checkRootPath(String uri, QueryStringDecoder queryStringDecoder) {
        if (!PathUtil.getRootPath(queryStringDecoder.path()).equals(this.getRootPath())) {
            throw new JupiterException(StatusEnum.REQUEST_ERROR, uri);
        }
    }
}
