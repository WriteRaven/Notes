package com.writeraven.jupiter.mvc.server.action.param;

import java.util.HashMap;

/**
 * Function:
 *
 * @author  cyj
 *         Date: 2021/2/2 11:30
 * @since JDK 1.8
 */
public class ParamMap extends HashMap<String,Object> implements Param {



    @Override
    public String getString(String param) {
        return super.get(param).toString();
    }

    @Override
    public Integer getInteger(String param) {
        return Integer.parseInt(super.get(param).toString());
    }

    @Override
    public Long getLong(String param) {
        return (Long) super.get(param);
    }

    @Override
    public Double getDouble(String param) {
        return (Double) super.get(param);
    }

    @Override
    public Float getFloat(String param) {
        return (Float) super.get(param);
    }

    @Override
    public Boolean getBoolean(String param) {
        return (Boolean) super.get(param);
    }
}
