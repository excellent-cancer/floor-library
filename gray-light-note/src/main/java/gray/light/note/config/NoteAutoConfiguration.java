package gray.light.note.config;


import gray.light.book.annotation.BookSupport;
import gray.light.book.job.CheckDocumentRepositoryJob;
import gray.light.book.job.UploadDocumentRepositoryJob;
import gray.light.book.service.BookRepositoryCacheService;
import gray.light.book.service.BookService;
import gray.light.book.service.BookSourceService;
import gray.light.note.service.NoteService;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.entity.ProjectStatus;
import gray.light.owner.service.ProjectDetailsService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import perishing.constraint.jdbc.Page;

import java.util.List;
import java.util.function.Supplier;

@BookSupport
@Configuration
@ComponentScan({"gray.light.note.handler", "gray.light.note.router"})
public class NoteAutoConfiguration {

    @Configuration
    @ConditionalOnBean(BookRepositoryCacheService.class)
    @ConditionalOnProperty(value = "gray.light.note.check-update.enabled", matchIfMissing = true)
    @RequiredArgsConstructor
    public static class CheckDocumentRepositoryConfiguration {

        private final NoteService noteService;

        private final BookRepositoryCacheService bookRepositoryCacheService;

        private final ProjectDetailsService projectDetailsService;

        @Bean("checkRepositoryDataMap")
        public JobDataMap jobData() {
            JobDataMap jobDataMap = new JobDataMap();

            Supplier<List<ProjectDetails>> syncStatusProjectDetails = () -> noteService.findProjectDetailsByStatus(ProjectStatus.SYNC, Page.unlimited());

            jobDataMap.put("projectDetailsService", projectDetailsService);
            jobDataMap.put("bookRepositoryCacheService", bookRepositoryCacheService);
            jobDataMap.put("syncStatusProjectDetails", syncStatusProjectDetails);

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
    @ConditionalOnProperty(value = "gray.light.note.upload-pending.enabled", matchIfMissing = true)
    @RequiredArgsConstructor
    @ConditionalOnBean({BookSourceService.class, BookRepositoryCacheService.class})
    public static class UploadDocumentRepositoryConfiguration {

        final NoteService noteService;

        final BookService bookService;

        final BookSourceService bookSourceService;

        final BookRepositoryCacheService bookRepositoryCacheService;

        @Bean("updateRepositoryDataMap")
        public JobDataMap jobData() {
            JobDataMap jobDataMap = new JobDataMap();

            Supplier<List<ProjectDetails>> initStatusProjectDetails = () -> noteService.findProjectDetailsByStatus(ProjectStatus.INIT, Page.unlimited());

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
