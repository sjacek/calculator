package pl.sjacek.calculator.controller.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

/**
 * Bean calculating e^x asynchronically
 */
@Slf4j
public class IntegralBean {

    /**
     * The main method
     * @param n repetions
     * @param x x
     * @return result of the calculation
     */
    @Async
    public CompletableFuture<Double> runTask (int n, double x) {
        log.debug("Running task thread: {}", Thread.currentThread().getName());

        return new CompletableFuture<Double>() {
            @Override
            public Double get() {
                double sum = 1d;
                for (int i = n - 1; i > 0; --i)
                    sum = 1 + x * sum / i;
                return sum;
            }
        };
    }
}
