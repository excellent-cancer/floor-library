package org.excellent.cancer.experimental.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 *
 * @author XyParaCrim
 */
@Configuration
@ConditionalOnProperty("excellent.cancer.jdbc.enabled")
@EnableJdbcRepositories("org.excellent.cancer.experimental.application.jdbc.repositories")
public class JdbcRepositoryConfiguration extends AbstractJdbcConfiguration {

    private final DataSource dataSource;

    @Autowired
    public JdbcRepositoryConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public NamedParameterJdbcOperations operations() {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
