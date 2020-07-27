package gray.light.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * 默认自动配置
 *
 * @author XyParaCrim
 */
@Slf4j
@Configuration
@EnableRedisWebSession
public class ExperimentalAutoConfiguration {

    @Configuration
    @EnableWebFlux
    public static class GlobCorsConf implements WebFluxConfigurer {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowCredentials(false)
                    .allowedOrigins("*")
                    .allowedHeaders("*")
                    .allowedMethods("*")
                    .exposedHeaders(HttpHeaders.SET_COOKIE);
        }
    }

    @Component
    public static class SessionFilter implements WebFilter {

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            return exchange.
                    getSession().
                    doOnSuccess(WebSession::start).
                    then(chain.filter(exchange));
        }

    }

    @Slf4j
    @Component
    @Intercepts({
            @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
            @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
            @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
    })
    public static class SqlExecuteTimeCountInterceptor implements Interceptor {

        /**
         * 打印的参数字符串的最大长度
         */
        private final static int MAX_PARAM_LENGTH = 50;

        /**
         * 记录的最大SQL长度
         */
        private final static int MAX_SQL_LENGTH = 200;

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
                sql = formatSQL(sql, parameterObject, parameterMappingList);

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
        private String formatSQL(String sql, Object parameterObject, List<ParameterMapping> parameterMappingList) {
            // 输入sql字符串空判断
            if (sql == null || sql.length() == 0) {
                return "";
            }
            // 美化sql
            sql = beautifySql(sql);
            // 不传参数的场景，直接把sql美化一下返回出去
            if (parameterObject == null || parameterMappingList == null || parameterMappingList.size() == 0) {
                return sql;
            }
            return LimitSQLLength(sql);
        }


        /**
         * 返回限制长度之后的SQL语句
         *
         *
         * @param sql 原始SQL语句
         */
        private String LimitSQLLength(String sql) {
            if (sql == null || sql.length() == 0) {
                return "";
            }
            if (sql.length() > MAX_SQL_LENGTH) {
                return sql.substring(0, MAX_SQL_LENGTH);
            } else {
                return sql;
            }
        }


        /**
         * 替换SQL 中? 所对应的值, 只保留前50个字符
         *
         * @param sql     sql语句
         * @param valueOf ?对应的值
         */
        private String replaceValue(String sql, String valueOf) {
            //超过50个字符只取前50个
            if (valueOf != null && valueOf.length() > MAX_PARAM_LENGTH) {
                valueOf = valueOf.substring(0, MAX_PARAM_LENGTH);
            }
            sql = sql.replaceFirst("\\?", valueOf);
            return sql;
        }

        /**
         * 美化sql
         *
         * @param sql sql语句
         */
        private String beautifySql(String sql) {
            sql = sql.replaceAll("[\\s\n ]+", "  ");
            return sql;
        }
    }

}
