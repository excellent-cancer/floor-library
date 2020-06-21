package gray.light.config;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.TrackerClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 初始化fastdfs的tacker-client对象，用于与fastdfs的tracker-server通讯
 *
 * @author XyParaCrim
 */
@Configuration
@AutoConfigureAfter(ExperimentalAutoConfiguration.class)
@ConditionalOnBean(ExcellentCancerProperties.class)
@ConditionalOnProperty("excellent.cancer.fastdfs.enabled")
public class FastdfsConfiguration {

    /**
     * 初始化fastdfs的全局配置，另外，初始化全局{@link org.csource.fastdfs.TrackerGroup}实例，
     * 从而获取{@link TrackerClient}与文件服务器通讯的客户端
     *
     * @param excellentCancerProperties spring属性
     * @return 文件服务器通讯的客户端
     */
    @Bean
    public static TrackerClient trackerClient(ExcellentCancerProperties excellentCancerProperties) {
        try {
            ClientGlobal.initByProperties(excellentCancerProperties.getFastdfs().getProperties());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize client of fastdfs", e);
        }

        return new TrackerClient();
    }

}














