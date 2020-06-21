package gray.light.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 特殊制定的Spring配置文件的属性
 *
 * @author XyParaCrim
 */
@Data
@ConfigurationProperties("excellent.cancer")
public class ExcellentCancerProperties {

    private OwnerProperties owner;

    private FastdfsProperties fastdfs;

    private RunningProperties running;

}
