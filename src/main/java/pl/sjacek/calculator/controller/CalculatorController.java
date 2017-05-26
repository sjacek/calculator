package pl.sjacek.calculator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pl.sjacek.calculator.Calculator;
import pl.sjacek.calculator.CalculatorException;
import pl.sjacek.calculator.controller.async.IntegralBean;
import pl.sjacek.calculator.model.Calculation;
import pl.sjacek.calculator.repositories.CalculationRepository;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by jacek.sztajnke on 2017-05-23.
 */
@RestController
@RequestMapping("/v1/calculator")
@Slf4j
public class CalculatorController {

    @Autowired
    private CalculationRepository calculationRepository;

    private static final String CALCULATOR_HTML = "calculator.html";

    @PostMapping(path = "/calculate", consumes = APPLICATION_JSON_VALUE, headers = "Accept=application/json")
    public ModelMap calculate(@RequestBody CalculateParam param) throws CalculatorException {
        log.debug("calculate({})", param.getExpression());

        ModelMap model = new ModelMap(CALCULATOR_HTML);

        if (param.getExpression() == null) {
            model.addAttribute("");
            return model;
        }

        calculationRepository.save(Calculation.builder()
                .expression(param.getExpression())
                .datetime(new Date())
                .build());

        model.addAttribute(Double.toString(Calculator.calculate(param.getExpression())));
        return model;
    }

    @Autowired
    private IntegralBean integralBean;

    @Autowired
    @Qualifier("taskExecutor")
    Executor executor;

    @PostMapping(path = "/calculateIntegral", consumes = APPLICATION_JSON_VALUE, headers = "Accept=application/json")
    public ModelMap calculateIntegral(@RequestBody CalculateIntegralParam param) throws CalculatorException {
        log.debug("calculateIntegral: {}", param.toString());

//        IntegralBean bean = context.getBean(IntegralBean.class);
        log.debug("calling IntegralBean#runTask() thread: {}", Thread.currentThread().getName());


        CompletableFuture<Double> result = integralBean.runTask();

        log.debug("Task started");
//        ConcurrentTaskExecutor exec = (ConcurrentTaskExecutor) context.getBean("taskExecutor");
        ExecutorService es = (ExecutorService) ((ConcurrentTaskExecutor)executor).getConcurrentExecutor();
        es.shutdown();

        try {
            log.debug("Result: {}", result.get());
        } catch (InterruptedException | ExecutionException ex) {
            log.error(ex.getMessage());
        }
        ModelMap model = new ModelMap(CALCULATOR_HTML);
        model.addAttribute("");
        return model;
    }

    @ExceptionHandler(CalculatorException.class)
    public ModelMap handleCustomException(CalculatorException ex) {
        log.warn(ex.getMessage(), ex);
        ModelMap model = new ModelMap(CALCULATOR_HTML);
        model.addAttribute(ex.getMessage());
        return model;
    }

    @ExceptionHandler(Exception.class)
    public ModelMap handleAllException(Exception ex) {
        log.warn(ex.getMessage(), ex);
        ModelMap model = new ModelMap(CALCULATOR_HTML);
        StringBuilder message = new StringBuilder();
        message.append(ex.getClass().getName()).append(": ");
        if (ex.getMessage() != null)
            message.append(ex.getMessage());
        model.addAttribute(message.toString());
        return model;
    }
}
