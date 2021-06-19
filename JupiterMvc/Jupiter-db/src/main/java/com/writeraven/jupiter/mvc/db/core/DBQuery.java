package com.writeraven.jupiter.mvc.db.core;


import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;
import com.writeraven.jupiter.mvc.db.annotation.TableName;
import com.writeraven.jupiter.mvc.db.sqlsession.SqlSessionFactory;
import com.writeraven.jupiter.mvc.db.model.Model;
import com.writeraven.jupiter.mvc.db.reflect.Instance;
import com.writeraven.jupiter.mvc.db.reflect.ReflectTools;
import lombok.extern.slf4j.Slf4j;
import com.writeraven.jupiter.mvc.db.sql.Condition;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
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
 * Date: 2019-11-19 19:40
 * @since JDK 1.8
 */
@Slf4j
public final class DBQuery<T extends Model> extends SqlSessionFactory {

    private Class<T> targetClass;

    private List<Condition> conditions = new ArrayList<>();

    private DbTable dbTable;

    private Map<String, DbColumn> columnMap = new HashMap<>();

    public DBQuery(Class<T> clazz) {
        this.targetClass = clazz;
        dbTable = super.getSession().addTable(targetClass.getAnnotation(TableName.class).value());
    }

    public DBQuery addCondition(Condition condition) {
        conditions.add(condition);
        return this;
    }

    public List<T> all() {
        List<T> result = null;
        String sql = buildSQL();
        Statement statement = null;
        try {
            statement = super.getSession().getConnection().createStatement();
            log.debug("execute sql>>>>>{}", sql);
            ResultSet resultSet = statement.executeQuery(sql);
            result = new ArrayList<>();

            Map<String, Object> fields = new HashMap<>(8);
            while (resultSet.next()) {
                for (Field field : targetClass.getDeclaredFields()) {
                    String dbField = ReflectTools.getDbField(field);

                    //get value from db
                    Method method = resultSet.getClass().getMethod(ReflectTools.getMethod(field.getType().getName()), String.class);

                    Object value = method.invoke(resultSet, dbField);

                    fields.put(field.getName(), value);

                }

                // transfer DB value to custom model
                T transfer = Instance.transfer(targetClass, fields);
                result.add(transfer);
            }
            resultSet.close();
        } catch (Exception e) {
            log.error("Exception", e);
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("Exception", e);
            }
        }

        return result;
    }

    public T first() {
        return this.all().get(0);
    }

    private String buildSQL() {
        SelectQuery selectQuery = new SelectQuery();

        for (Field field : targetClass.getDeclaredFields()) {
            String dbField = ReflectTools.getDbField(field);

            DbColumn dbColumn = dbTable.addColumn(dbField);
            selectQuery.addColumns(dbColumn);
            columnMap.put(dbField, dbColumn);
        }

        for (Condition condition : conditions) {
            Condition.Filter filter = condition.getCondition();
            DbColumn dbColumn = columnMap.get(filter.getFiled());
            selectQuery.addCondition(BinaryCondition.equalTo(dbColumn, filter.getValue()));
        }

        return selectQuery.validate().toString();

    }


}
