package gray.light.config;

/*import gray.light.document.service.DocumentRelationService;
import gray.light.document.service.DocumentRepositoryCacheService;
import gray.light.document.service.DocumentSourceService;
import gray.light.job.CheckDocumentRepositoryJob;
import gray.light.job.UploadDocumentRepositoryJob;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;*/

/**
 * @author XyParaCrim
 */
/*@CommonsLog
@Configuration
@ConditionalOnProperty("excellent.cancer.running.enabled")*/
public class QuartzAutoConfiguration {

/*    @Configuration
    @ConditionalOnProperty("excellent.cancer.running.enabled")
    public static class CheckDocumentRepositoryConfiguration {

        @Bean("checkRepositoryDataMap")
        public JobDataMap jobData(DocumentRelationService documentRelationService,
                                  DocumentRepositoryCacheService documentRepositoryCacheService) {
            JobDataMap jobDataMap = new JobDataMap();

            jobDataMap.put("documentService", documentRelationService);
            jobDataMap.put("documentRepositoryCacheService", documentRepositoryCacheService);

            return jobDataMap;
        }

        @Bean("checkDocumentRepositoryJobDetail")
        public JobDetail checkDocumentRepositoryJobDetail(JobDataMap checkRepositoryDataMap) {
            return JobBuilder.
                    newJob(CheckDocumentRepositoryJob.class).
                    usingJobData(checkRepositoryDataMap).
                    storeDurably().
                    build();
        }

        @Bean
        public Trigger checkDocumentRepositoryTrigger(JobDetail checkDocumentRepositoryJobDetail) {
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.
                    simpleSchedule().
                    withIntervalInMinutes(1).
                    repeatForever();

            return TriggerBuilder.
                    newTrigger().
                    forJob(checkDocumentRepositoryJobDetail).
                    withSchedule(scheduleBuilder).
                    build();
        }

    }

    @Configuration
    @ConditionalOnProperty("excellent.cancer.running.enabled")
    public static class UploadDocumentRepositoryConfiguration {

        @Bean("updateRepositoryDataMap")
        public JobDataMap jobData(DocumentRelationService documentService,
                                  DocumentSourceService documentSourceService,
                                  DocumentRepositoryCacheService documentRepositoryCacheService) {
            JobDataMap jobDataMap = new JobDataMap();

            jobDataMap.put("documentService", documentService);
            jobDataMap.put("documentSourceService", documentSourceService);
            jobDataMap.put("documentRepositoryCacheService", documentRepositoryCacheService);

            return jobDataMap;
        }


        @Bean("uploadDocumentRepositoryJobDetail")
        public JobDetail uploadDocumentRepositoryJobDetail(JobDataMap updateRepositoryDataMap) {
            return JobBuilder.
                    newJob(UploadDocumentRepositoryJob.class).
                    usingJobData(updateRepositoryDataMap).
                    storeDurably().
                    build();
        }

        @Bean
        public Trigger uploadDocumentRepositoryTrigger(JobDetail uploadDocumentRepositoryJobDetail) {
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.
                    simpleSchedule().
                    withIntervalInMinutes(1).
                    repeatForever();

            return TriggerBuilder.
                    newTrigger().
                    forJob(uploadDocumentRepositoryJobDetail).
                    withSchedule(scheduleBuilder).
                    build();
        }
    }*/

}
