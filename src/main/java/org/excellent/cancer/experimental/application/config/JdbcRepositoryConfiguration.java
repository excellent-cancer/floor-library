package org.excellent.cancer.experimental.application.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.conversion.RelationalConverter;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 * @author XyParaCrim
 */
@Configuration
@ConditionalOnProperty("excellent.cancer.jdbc.enabled")
@EnableJdbcRepositories("org.excellent.cancer.experimental.application.jdbc.repositories")
public class JdbcRepositoryConfiguration extends AbstractJdbcConfiguration {

    @Bean
    public NamedParameterJdbcOperations operations(JdbcOperations operations) {
        return new NamedParameterJdbcTemplate(operations);
    }

}
