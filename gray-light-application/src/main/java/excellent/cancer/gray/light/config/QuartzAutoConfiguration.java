package excellent.cancer.gray.light.config;

import lombok.extern.apachecommons.CommonsLog;
import org.quartz.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author XyParaCrim
 */
@Configuration
@ConditionalOnProperty("excellent.cancer.running.enabled")
public class QuartzAutoConfiguration {

    @Bean
    public JobDetail testJobDetail() {
        return JobBuilder.newJob(TestJob.class).usingJobData("name", "yan").storeDurably().build();
    }

    @Bean
    public Trigger trigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.
                simpleSchedule().withIntervalInMinutes(1).repeatForever();

        return TriggerBuilder.newTrigger().forJob(testJobDetail()).withSchedule(scheduleBuilder).build();
    }


    @CommonsLog
    public static class TestJob extends QuartzJobBean {

        private String name;

        @Override
        protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
            log.error("执行任务");
            log.error(name);
        }
    }

}
