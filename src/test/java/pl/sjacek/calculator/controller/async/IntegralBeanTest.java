package pl.sjacek.calculator.controller.async;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by jacek on 28.05.17.
 */
public class IntegralBeanTest {

    @Test
    public void integralTest() {
        double begin = 0;
        double end = 10;
        int subintervals = 1000;
        int repetitions = 1000;

        double dx = (end - begin) / subintervals;

        List<Double> input = new ArrayList<>();
        for (int i = 1; i <= subintervals; i++)
            input.add(begin + i * dx);

        IntegralBean integralBean = new IntegralBean();

        List<CompletableFuture<Double>> results = input.stream().map(d -> integralBean.runTask(repetitions, d)).collect(Collectors.toList());

        final double[] ret = new double[1];
        ret[0] = 0;
        final String[] errorMessage = new String[1];
        results.forEach(result -> {
            try {
                ret[0] += result.get();
            } catch (InterruptedException | ExecutionException ex) {
                errorMessage[0] = ex.getMessage();
            }
        });
        ret[0] *= dx;

        assertEquals(22135.8, ret[0], 0.03d);
    }
}