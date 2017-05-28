package pl.sjacek.calculator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pl.sjacek.calculator.Calculator;
import pl.sjacek.calculator.CalculatorException;
import pl.sjacek.calculator.controller.async.IntegralBean;
import pl.sjacek.calculator.dto.CalculateDTO;
import pl.sjacek.calculator.dto.CalculateIntegralDTO;
import pl.sjacek.calculator.model.Calculation;
import pl.sjacek.calculator.service.CalculationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by jacek.sztajnke on 2017-05-23.
 */
@RestController
@RequestMapping("/v1/calculator")
@Slf4j
public class CalculatorController {

//    @Autowired
//    private CalculationRepository calculationRepository;
    @Autowired
    private CalculationService calculationService;

    @Autowired
    private IntegralBean integralBean;

    private static final String CALCULATOR_HTML = "calculator.html";

//    public CalculatorController(CalculationRepository calculationRepository, IntegralBean integralBean, Executor executor) {
//        this.calculationRepository = calculationRepository;
//        this.integralBean = integralBean;
//        this.executor = executor;
//    }

    @PostMapping(path = "/calculate", consumes = APPLICATION_JSON_VALUE, headers = "Accept=application/json")
    public ModelMap calculate(@RequestBody CalculateDTO dto) throws CalculatorException {
        log.debug("calculate({})", dto.getExpression());

        ModelMap model = new ModelMap(CALCULATOR_HTML);

        if (dto.getExpression() == null) {
            model.addAttribute("");
            return model;
        }

        calculationService.save(dto);

        model.addAttribute(Double.toString(Calculator.calculate(dto.getExpression())));
        return model;
    }

    @PostMapping(path = "/calculateIntegral", consumes = APPLICATION_JSON_VALUE, headers = "Accept=application/json")
    public ModelMap calculateIntegral(@RequestBody CalculateIntegralDTO dto) throws CalculatorException {
        log.debug("calculateIntegral: {}", dto.toString());

//        IntegralBean bean = context.getBean(IntegralBean.class);
        log.debug("calling IntegralBean#runTask() thread: {}", Thread.currentThread().getName());

        List<Double> input = new ArrayList<>();
        for (int i = 0; i < dto.getThreads(); i++) input.add((double)i);

        List<CompletableFuture<Double>> results = input.stream().map(d -> integralBean.runTask(d)).collect(Collectors.toList());

        log.debug("Tasks started");
//        ExecutorService es = (ExecutorService) ((ConcurrentTaskExecutor)executor).getConcurrentExecutor();
//        es.shutdown();

        results.forEach(result -> {
            try {
                log.debug("Result:{}", result.get());
            } catch (InterruptedException | ExecutionException ex) {
                log.error(ex.getMessage());
            }
        });

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
