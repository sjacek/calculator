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
    public CompletableFuture<Double> runTask (int n, double d) {
        log.info("Running task thread: {}", Thread.currentThread().getName());

        return new CompletableFuture<Double>() {
            @Override
            public Double get() {
                double sum = 1d;
                for (int i = n - 1; i > 0; --i)
                    sum = 1 + d * sum / i;
                return sum;
            }
        };
    }
}
