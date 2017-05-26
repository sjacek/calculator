package pl.sjacek.calculator.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import pl.sjacek.calculator.controller.async.IntegralBean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by jacek on 26.05.17.
 */
@EnableAsync
@Configuration
public class IntegralCalculationConfig implements AsyncConfigurer {
    @Bean
    public IntegralBean myBean () {
        return new IntegralBean();
    }

    @Bean
    @Qualifier("taskExecutor")
    @Override
    public Executor getAsyncExecutor () {
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3));
    }

//    @Bean
//    public TaskExecutor taskExecutor() {
//        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3));
//    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler () {
        return (throwable, method, objects) -> System.out.println("-- exception handler -- "+throwable);
    }
}