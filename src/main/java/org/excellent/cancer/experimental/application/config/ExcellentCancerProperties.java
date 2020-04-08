package org.excellent.cancer.experimental.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

/**
 * 特殊制定的Spring配置文件的属性
 *
 * @author XyParaCrim
 */
@Data
@ImportResource("excellent-cancer.yml")
@ConfigurationProperties("excellent.cancer")
public class ExcellentCancerProperties {

    private OwnerProperties owner;

    private FastdfsProperties fastdfs;

}
