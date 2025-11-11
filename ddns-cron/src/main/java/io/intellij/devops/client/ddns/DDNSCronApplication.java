package io.intellij.devops.client.ddns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * DDNSCronApplication
 *
 * @author tech@intellij.io
 */
@SpringBootApplication
@EnableScheduling
public class DDNSCronApplication {

    static void main(String[] args) {
        SpringApplication.run(DDNSCronApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .connectTimeout(Duration.of(3, ChronoUnit.SECONDS))
                .readTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler ts = new ThreadPoolTaskScheduler();
        ts.setPoolSize(2);
        ts.setThreadNamePrefix("ddns-schedule-");
        ts.setVirtualThreads(true); // 自动使用虚拟线程执行任务
        ts.setRemoveOnCancelPolicy(true);
        ts.setAwaitTerminationSeconds(60);
        ts.setWaitForTasksToCompleteOnShutdown(true);
        return ts;
    }

}
