package excellent.cancer.gray.light.config;

import excellent.cancer.gray.light.document.DocumentRepositoryDatabase;
import excellent.cancer.gray.light.job.UploadDocumentRepositoryJob;
import excellent.cancer.gray.light.service.DocumentRelationService;
import lombok.extern.apachecommons.CommonsLog;
import org.csource.fastdfs.TrackerClient;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author XyParaCrim
 */
@CommonsLog
@Configuration
@ConditionalOnProperty("excellent.cancer.running.enabled")
public class QuartzAutoConfiguration {

    @Bean
    public DocumentRepositoryDatabase documentRepositoryDatabase(ExcellentCancerProperties excellentCancerProperties) throws IOException {
        return new DocumentRepositoryDatabase(excellentCancerProperties.getRunning().getDocumentRepositories(), true);
    }

//    @Bean("checkDocumentRepositoryJobDetail")
//    public JobDetail checkDocumentRepositoryJobDetail(DocumentRelationService documentRelationService, DocumentRepositoryDatabase documentRepositoryDatabase) {
//        JobDataMap jobDataMap = new JobDataMap();
//
//        jobDataMap.put("documentService", documentRelationService);
//        jobDataMap.put("repositoryDatabase", documentRepositoryDatabase);
//
//        return JobBuilder.
//                newJob(CheckDocumentRepositoryJob.class).
//                usingJobData(jobDataMap).
//                storeDurably().
//                build();
//    }


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

    @Bean("uploadDocumentRepositoryJobDetail")
    public JobDetail uploadDocumentRepositoryJobDetail(DocumentRelationService documentService,
                                                       TrackerClient trackerClient,
                                                       DocumentRepositoryDatabase repositoryDatabase) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("documentService", documentService);
        jobDataMap.put("repositoryDatabase", repositoryDatabase);
        jobDataMap.put("trackerClient", trackerClient);

        return JobBuilder.
                newJob(UploadDocumentRepositoryJob.class).
                usingJobData(jobDataMap).
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
