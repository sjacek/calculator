package pl.sjacek.calculator.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Value("${calculator.threads.pool-size:20}")
    private int poolSize;

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(poolSize));
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler () {
        return (throwable, method, objects) -> System.out.println("-- exception handler -- "+throwable);
    }
}