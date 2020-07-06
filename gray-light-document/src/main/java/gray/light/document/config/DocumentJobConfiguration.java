package gray.light.document.config;

import gray.light.book.job.CheckDocumentRepositoryJob;
import gray.light.book.job.UploadDocumentRepositoryJob;
import gray.light.book.service.BookService;
import gray.light.book.service.BookSourceService;
import gray.light.document.service.DocumentRelationService;
import gray.light.book.service.BookRepositoryCacheService;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.entity.ProjectStatus;
import gray.light.owner.service.ProjectDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import perishing.constraint.jdbc.Page;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author XyParaCrim
 */
@CommonsLog
@Configuration
public class DocumentJobConfiguration {

    @Configuration
    @ConditionalOnClass({DocumentRelationService.class, BookRepositoryCacheService.class})
    @ConditionalOnProperty(value = "gray.light.document.check-update.enabled", matchIfMissing = true)
    @RequiredArgsConstructor
    public static class CheckDocumentRepositoryConfiguration {

        private final DocumentRelationService documentRelationService;

        private final BookRepositoryCacheService bookRepositoryCacheService;

        private final ProjectDetailsService projectDetailsService;

        private final BookService bookService;

        private final BookSourceService bookSourceService;

        @Bean("checkRepositoryDataMap")
        public JobDataMap jobData() {
            JobDataMap jobDataMap = new JobDataMap();

            Supplier<List<ProjectDetails>> syncStatusProjectDetails = () -> documentRelationService.findProjectDetailsByStatus(ProjectStatus.SYNC, Page.unlimited());

            jobDataMap.put("projectDetailsService", projectDetailsService);
            jobDataMap.put("bookRepositoryCacheService", bookRepositoryCacheService);
            jobDataMap.put("syncStatusProjectDetails", syncStatusProjectDetails);
            jobDataMap.put("bookService", bookService);
            jobDataMap.put("bookSourceService", bookSourceService);

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
                    withIntervalInHours(1).
                    repeatForever();

            return TriggerBuilder.
                    newTrigger().
                    forJob(checkDocumentRepositoryJobDetail).
                    withSchedule(scheduleBuilder).
                    build();
        }

    }

    @Configuration
    @ConditionalOnProperty(value = "gray.light.document.upload-pending.enabled", matchIfMissing = true)
    @RequiredArgsConstructor
    @ConditionalOnClass({BookSourceService.class, BookRepositoryCacheService.class})
    public static class UploadDocumentRepositoryConfiguration {

        final DocumentRelationService documentService;

        final BookService bookService;

        final BookSourceService bookSourceService;

        final BookRepositoryCacheService bookRepositoryCacheService;

        @Bean("updateRepositoryDataMap")
        public JobDataMap jobData() {
            JobDataMap jobDataMap = new JobDataMap();

            Supplier<List<ProjectDetails>> initStatusProjectDetails = () -> documentService.findProjectDetailsByStatus(ProjectStatus.INIT, Page.unlimited());

            jobDataMap.put("bookService", bookService);
            jobDataMap.put("bookSourceService", bookSourceService);
            jobDataMap.put("bookRepositoryCacheService", bookRepositoryCacheService);
            jobDataMap.put("initStatusProjectDetails", initStatusProjectDetails);

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
                    withIntervalInHours(1).
                    repeatForever();

            return TriggerBuilder.
                    newTrigger().
                    forJob(uploadDocumentRepositoryJobDetail).
                    withSchedule(scheduleBuilder).
                    build();
        }
    }
}
