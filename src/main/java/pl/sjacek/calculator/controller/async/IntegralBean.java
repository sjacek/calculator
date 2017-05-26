package pl.sjacek.calculator.controller.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

/**
 * Created by jacek on 26.05.17.
 */
@Slf4j
public class IntegralBean {
    @Async
    public CompletableFuture<Double> runTask () {
        log.info("Running task  thread: {}", Thread.currentThread().getName());

        CompletableFuture<Double> result = new CompletableFuture<Double>() {
            @Override
            public Double get() {
                return 123.45;
            }
        };
        return result;
    }
}
