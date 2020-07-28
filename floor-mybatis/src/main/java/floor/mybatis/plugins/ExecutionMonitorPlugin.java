package floor.mybatis.plugins;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import perishing.constraint.jdbc.SqlSupport;

import java.sql.Statement;
import java.util.List;

/**
 * 统计Sql语句的执行时间
 *
 * @author XyParaCrim
 */
@Slf4j
@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
public class ExecutionMonitorPlugin implements Interceptor {

    /**
     *  设置警告阀值
     */
    private final static long WARN_TIME = 100L;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        long startTime = System.currentTimeMillis();
        StatementHandler statementHandler = (StatementHandler) target;
        try {
            return invocation.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            long timeCount = endTime - startTime;

            BoundSql boundSql = statementHandler.getBoundSql();
            String sql = boundSql.getSql();
            Object parameterObject = boundSql.getParameterObject();
            List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();

            // 格式化Sql语句，去除换行符，替换参数
            sql = format(sql, parameterObject, parameterMappingList);

            if (timeCount >= WARN_TIME) {
                log.warn("执行 SQL：[ {} ]执行耗时[ {} ms]", sql, timeCount);
            } else {
                log.info("执行 SQL：[ {} ]执行耗时[ {} ms]", sql, timeCount);
            }
        }
    }

    /**
     * 格式化/美化 SQL语句
     *
     * @param sql                  sql 语句
     * @param parameterObject      参数的Map
     * @param parameterMappingList 参数的List
     * @return 格式化之后的SQL
     */
    private String format(String sql, Object parameterObject, List<ParameterMapping> parameterMappingList) {

        sql = SqlSupport.beautifySql(sql);

        // 不传参数的场景，直接把sql美化一下返回出去
        if (parameterObject == null || parameterMappingList == null || parameterMappingList.size() == 0) {
            return sql;
        }
        return SqlSupport.limitSQLLength(sql);
    }

}
