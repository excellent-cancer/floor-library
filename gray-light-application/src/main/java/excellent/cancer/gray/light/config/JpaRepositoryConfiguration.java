package excellent.cancer.gray.light.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("excellent.cancer.gray.light.jdbc.repositories")
@EnableTransactionManagement
@EnableJpaAuditing
public class JpaRepositoryConfiguration {
}
