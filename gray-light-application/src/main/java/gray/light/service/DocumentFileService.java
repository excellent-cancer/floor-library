package gray.light.service;

import lombok.extern.apachecommons.CommonsLog;
import org.csource.fastdfs.TrackerClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

/**
 * 定义有关于文档文件的上传、下载和更改
 *
 * @author XyParaCrim
 */
@Service
@CommonsLog
@ConditionalOnBean(TrackerClient.class)
public class DocumentFileService {

/*    private final TrackerClient trackerClient;

    @Autowired
    public DocumentFileService(TrackerClient trackerClient) {
        this.trackerClient = trackerClient;
    }*/

}