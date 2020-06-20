package excellent.cancer.gray.light.config;

import excellent.cancer.floor.repository.LocalRepositoryDatabase;
import excellent.cancer.floor.repository.RepositoryDatabase;
import excellent.cancer.gray.light.job.CheckDocumentRepositoryJob;
import excellent.cancer.gray.light.job.UploadDocumentRepositoryJob;
import excellent.cancer.gray.light.service.DocumentRelationService;
import lombok.extern.apachecommons.CommonsLog;
import org.csource.fastdfs.TrackerClient;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import perishing.constraint.treasure.chest.converter.Converters;

import java.io.IOException;

/**
 * @author XyParaCrim
 */
@CommonsLog
@Configuration
@ConditionalOnProperty("excellent.cancer.running.enabled")
public class QuartzAutoConfiguration {

    @Bean
    public RepositoryDatabase<Long, Long> documentRepositoryDatabase(ExcellentCancerProperties excellentCancerProperties) throws IOException {
        String documentRepositoriesLocation = excellentCancerProperties.getRunning().getDocumentRepositories();

        return LocalRepositoryDatabase.of(documentRepositoriesLocation, Converters.LONG_STRING);
    }

    @Configuration
    public static class CheckDocumentRepositoryConfiguration {

        @Bean("checkRepositoryDataMap")
        public JobDataMap jobData(DocumentRelationService documentRelationService, RepositoryDatabase<Long, Long> documentRepositoryDatabase) {
            JobDataMap jobDataMap = new JobDataMap();

            jobDataMap.put("documentService", documentRelationService);
            jobDataMap.put("repositoryDatabase", documentRepositoryDatabase);

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
    public static class UploadDocumentRepositoryConfiguration {

        @Bean("updateRepositoryDataMap")
        public JobDataMap jobData(DocumentRelationService documentService,
                                  TrackerClient trackerClient,
                                  RepositoryDatabase<Long, Long> repositoryDatabase) {
            JobDataMap jobDataMap = new JobDataMap();

            jobDataMap.put("documentService", documentService);
            jobDataMap.put("repositoryDatabase", repositoryDatabase);
            jobDataMap.put("trackerClient", trackerClient);

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
