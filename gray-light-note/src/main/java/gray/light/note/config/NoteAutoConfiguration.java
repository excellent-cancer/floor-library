package gray.light.note.config;


import gray.light.book.job.CheckDocumentRepositoryJob;
import gray.light.book.job.UploadDocumentRepositoryJob;
import gray.light.book.service.BookRepositoryCacheService;
import gray.light.book.service.BookService;
import gray.light.book.service.BookSourceService;
import gray.light.note.handler.NoteHandler;
import gray.light.note.router.PersonalNoteRouter;
import gray.light.note.service.NoteService;
import gray.light.owner.entity.ProjectDetails;
import gray.light.owner.entity.ProjectStatus;
import gray.light.owner.service.ProjectDetailsService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import perishing.constraint.jdbc.Page;

import java.util.List;
import java.util.function.Supplier;

@Configuration
@ConditionalOnProperty(value = "gray.light.note.enabled", matchIfMissing = true)
@Import({NoteService.class, NoteHandler.class, PersonalNoteRouter.class})
public class NoteAutoConfiguration {

    @Configuration
    @ConditionalOnClass(BookRepositoryCacheService.class)
    @ConditionalOnProperty(value = "gray.light.note.check-update.enabled", matchIfMissing = true)
    @RequiredArgsConstructor
    public static class CheckNoteRepositoryConfiguration {

        private final NoteService noteService;

        private final BookRepositoryCacheService bookRepositoryCacheService;

        private final ProjectDetailsService projectDetailsService;

        private final BookService bookService;

        private final BookSourceService bookSourceService;

        @Bean("checkNoteRepositoryDataMap")
        public JobDataMap jobData() {
            JobDataMap jobDataMap = new JobDataMap();

            Supplier<List<ProjectDetails>> syncStatusProjectDetails = () -> noteService.findProjectDetailsByStatus(ProjectStatus.SYNC, Page.unlimited());

            jobDataMap.put("projectDetailsService", projectDetailsService);
            jobDataMap.put("bookRepositoryCacheService", bookRepositoryCacheService);
            jobDataMap.put("syncStatusProjectDetails", syncStatusProjectDetails);
            jobDataMap.put("bookService", bookService);
            jobDataMap.put("bookSourceService", bookSourceService);

            return jobDataMap;
        }

        @Bean("checkNoteRepositoryJobDetail")
        public JobDetail checkNoteRepositoryJobDetail(JobDataMap checkNoteRepositoryDataMap) {
            return JobBuilder.
                    newJob(CheckDocumentRepositoryJob.class).
                    usingJobData(checkNoteRepositoryDataMap).
                    storeDurably().
                    build();
        }

        @Bean
        public Trigger checkNoteRepositoryTrigger(JobDetail checkNoteRepositoryJobDetail) {
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.
                    simpleSchedule().
                    withIntervalInHours(1).
                    repeatForever();

            return TriggerBuilder.
                    newTrigger().
                    forJob(checkNoteRepositoryJobDetail).
                    withSchedule(scheduleBuilder).
                    build();
        }

    }

    @Configuration
    @ConditionalOnProperty(value = "gray.light.note.upload-pending.enabled", matchIfMissing = true)
    @RequiredArgsConstructor
    @ConditionalOnClass({BookSourceService.class, BookRepositoryCacheService.class})
    public static class UploadNoteRepositoryConfiguration {

        final NoteService noteService;

        final BookService bookService;

        final BookSourceService bookSourceService;

        final BookRepositoryCacheService bookRepositoryCacheService;

        @Bean("updateNoteRepositoryDataMap")
        public JobDataMap jobData() {
            JobDataMap jobDataMap = new JobDataMap();

            Supplier<List<ProjectDetails>> initStatusProjectDetails = () -> noteService.findProjectDetailsByStatus(ProjectStatus.INIT, Page.unlimited());

            jobDataMap.put("bookService", bookService);
            jobDataMap.put("bookSourceService", bookSourceService);
            jobDataMap.put("bookRepositoryCacheService", bookRepositoryCacheService);
            jobDataMap.put("initStatusProjectDetails", initStatusProjectDetails);

            return jobDataMap;
        }


        @Bean("uploadNoteRepositoryJobDetail")
        public JobDetail uploadNoteRepositoryJobDetail(JobDataMap updateNoteRepositoryDataMap) {
            return JobBuilder.
                    newJob(UploadDocumentRepositoryJob.class).
                    usingJobData(updateNoteRepositoryDataMap).
                    storeDurably().
                    build();
        }

        @Bean
        public Trigger uploadNoteRepositoryTrigger(JobDetail uploadNoteRepositoryJobDetail) {
            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.
                    simpleSchedule().
                    withIntervalInHours(1).
                    repeatForever();

            return TriggerBuilder.
                    newTrigger().
                    forJob(uploadNoteRepositoryJobDetail).
                    withSchedule(scheduleBuilder).
                    build();
        }
    }

}
