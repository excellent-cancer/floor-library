package gray.light.document;

import floor.file.storage.FileStorage;
import floor.repository.RepositoryDatabase;
import floor.repository.annotation.FloorRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import perishing.constraint.treasure.chest.StringTreasureChest;

/**
 * @author XyParaCrim
 */
@Configuration
@ConditionalOnClass({RepositoryDatabase.class, FileStorage.class})
@MapperScan(DocumentConfiguration.REPOSITORY_PACKAGE)
@ComponentScan(DocumentConfiguration.SERVICE_PACKAGE)
public class DocumentConfiguration {

    public static final String REPOSITORY_PACKAGE = "gray.light.document.repository";

    public static final String SERVICE_PACKAGE = "gray.light.document.service";

}
