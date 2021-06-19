package com.writeraven.jupiter.mvc.db.core.handle;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import com.writeraven.jupiter.mvc.db.annotation.TableName;
import com.writeraven.jupiter.mvc.db.annotation.PrimaryId;
import com.writeraven.jupiter.mvc.db.model.Model;
import com.writeraven.jupiter.mvc.db.reflect.Instance;
import com.writeraven.jupiter.mvc.db.reflect.ReflectTools;
import lombok.extern.slf4j.Slf4j;
import com.writeraven.jupiter.mvc.db.sqlsession.SqlSessionFactory;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function:
 *
 * @author cyj
 * Date: 2019-12-03 23:53
 * @since JDK 1.8
 */
@Slf4j
public class DBHandler extends SqlSessionFactory implements AbstractDBHandler {

    private DbTable dbTable;

    /**
     * todo
     * 1。 目标table name 的方法给抽象出来
     *
     * @param obj model of db entity
     * @return
     */

    @Override
    public int update(Object obj) {

        if (!(obj instanceof Model)) {
            return 0;
        }
        // 获取dbTable
        dbTable = super.getSession().addTable(obj.getClass().getAnnotation(TableName.class).value());

        Map<DbColumn, Integer> primaryCondition = new HashMap<>(1);
        UpdateQuery updateQuery = new UpdateQuery(dbTable);

        for (Field field : obj.getClass().getDeclaredFields()) {
            String dbField = ReflectTools.getDbField(field);

            Object filedValue = Instance.getFiledValue(obj, field);
            if (null == filedValue) {
                continue;
            }

            if (field.getAnnotation(PrimaryId.class) != null) {
                primaryCondition.put(dbTable.addColumn(dbField), (Integer) filedValue);
            } else {
                DbColumn dbColumn = dbTable.addColumn(dbField);
                updateQuery.addSetClause(dbColumn, filedValue);
            }

        }

        // updateQuery中增加主键
        for (Map.Entry<DbColumn, Integer> entry : primaryCondition.entrySet()) {
            updateQuery.addCondition(BinaryCondition.equalTo(entry.getKey(), entry.getValue()));
        }

        Statement statement = null;
        try {
            statement = super.getSession().getConnection().createStatement();
            log.info("hello world");
            return statement.executeUpdate(updateQuery.toString());
        } catch (SQLException e) {
            log.error("SQLException", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("SQLException", e);
            }
        }
        return 0;
    }

    @Override
    public void insert(Object obj) {
        dbTable = super.getSession().addTable(obj.getClass().getAnnotation(TableName.class).value());

        InsertQuery insertSelectQuery = new InsertQuery(dbTable);
        List<Field> values = new ArrayList<>();

        for (Field field : obj.getClass().getDeclaredFields()) {
            String dbField = ReflectTools.getDbField(field);
            Object filedValue = Instance.getFiledValue(obj, field);
            if (null == filedValue) {
                continue;
            }

            insertSelectQuery.addPreparedColumns(dbTable.addColumn(dbField));
            values.add(field);
        }

        log.debug("execute sql>>>>>{}", insertSelectQuery.validate().toString());
        StringBuffer sb = new StringBuffer();
        PreparedStatement statement = null;
        try {
            statement = super.getSession().getConnection().prepareStatement(insertSelectQuery.toString());
            // 确定field类型
            for (int i = 0; i < values.size(); i++) {
                Field value = values.get(i);
                if (value.getType() == Integer.class) {
                    statement.setInt(i + 1, (Integer) Instance.getFiledValue(obj, value));
                }
                if (value.getType() == String.class) {
                    statement.setString(i + 1, (String) Instance.getFiledValue(obj, value));
                }
                sb.append(value.getName() + "=" + Instance.getFiledValue(obj, value) + "\t");
            }
            log.debug("params >>>>>>>>>[{}]", sb.toString());
            statement.execute();
        } catch (SQLException e) {
            log.error("SQLException", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("SQLException", e);
            }
        }
    }

    @Override
    public void delete(Object obj) {
        dbTable = super.getSession().addTable(obj.getClass().getAnnotation(TableName.class).value());

        DeleteQuery deleteQuery = new DeleteQuery(dbTable);

        // todo 增加id的条件
//        deleteQuery.addCondition(new ComboCondition(obj).addCondition(obj.g));

        log.debug("execute sql>>>>>{}", deleteQuery.validate().toString());

        PreparedStatement statement = null;
        try {
            statement = super.getSession().getConnection().prepareStatement(deleteQuery.toString());
            statement.execute();
        } catch (SQLException e) {
            log.error("SQLException", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("SQLException", e);
            }
        }
    }


}
