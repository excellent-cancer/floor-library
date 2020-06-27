package gray.light.document.config;

import gray.light.document.job.CheckDocumentRepositoryJob;
import gray.light.document.job.UploadDocumentRepositoryJob;
import gray.light.document.service.DocumentRelationService;
import gray.light.document.service.DocumentRepositoryCacheService;
import gray.light.document.service.DocumentSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author XyParaCrim
 */
@CommonsLog
@Configuration
public class DocumentJobConfiguration {

    @Configuration
    @ConditionalOnBean({DocumentRelationService.class, DocumentRepositoryCacheService.class})
    @ConditionalOnProperty(value = "gray.light.job.check-update.enabled", matchIfMissing = true)
    @RequiredArgsConstructor
    public static class CheckDocumentRepositoryConfiguration {

        private final DocumentRelationService documentRelationService;

        private final DocumentRepositoryCacheService documentRepositoryCacheService;


        @Bean("checkRepositoryDataMap")
        public JobDataMap jobData() {
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
    @ConditionalOnProperty(value = "gray.light.job.upload-pending.enabled", matchIfMissing = true)
    @RequiredArgsConstructor
    @ConditionalOnBean({DocumentSourceService.class, DocumentRepositoryCacheService.class})
    public static class UploadDocumentRepositoryConfiguration {

        final DocumentRelationService documentService;

        final DocumentSourceService documentSourceService;

        final DocumentRepositoryCacheService documentRepositoryCacheService;

        @Bean("updateRepositoryDataMap")
        public JobDataMap jobData() {
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
    }
}
