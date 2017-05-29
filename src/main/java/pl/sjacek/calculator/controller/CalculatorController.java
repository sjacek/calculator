package pl.sjacek.calculator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pl.sjacek.calculator.Calculator;
import pl.sjacek.calculator.CalculatorException;
import pl.sjacek.calculator.controller.async.IntegralBean;
import pl.sjacek.calculator.dto.CalculateDTO;
import pl.sjacek.calculator.dto.CalculateIntegralDTO;
import pl.sjacek.calculator.service.CalculationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by jacek.sztajnke on 2017-05-23.
 */
@RestController
@RequestMapping("/v1/calculator")
@Slf4j
public class CalculatorController {

    @Autowired
    private CalculationService calculationService;

    @Autowired
    private IntegralBean integralBean;

    @Autowired
    private MessageSource messageSource;

    private Locale localePl = new Locale.Builder().setLanguage("en").setRegion("US").build();

    private static final String CALCULATOR_HTML = "calculator.html";

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

        log.debug("calling IntegralBean#runTask() thread: {}", Thread.currentThread().getName());

        double dx = (dto.getIntervalEnd() - dto.getIntervalBegin()) / (double)dto.getThreads();

        List<Double> input = new ArrayList<>();
        for (int i = 1; i <= dto.getThreads(); i++)
            input.add(dto.getIntervalBegin() + i * dx);

        List<CompletableFuture<Double>> results = input.stream().map(d -> integralBean.runTask(dto.getRepetitions(), d)).collect(Collectors.toList());

        log.debug("Tasks started");

        final double[] ret = new double[1];
        ret[0] = 0;
        final String[] errorMessage = new String[1];
        results.forEach(result -> {
            try {
                log.debug("Result:{}", result.get());
                ret[0] += result.get();
            } catch (InterruptedException | ExecutionException ex) {
                log.error(ex.getMessage());
                errorMessage[0] = ex.getMessage();
            }
        });
        ret[0] *= dx;

        ModelMap model = new ModelMap(CALCULATOR_HTML);
        if (errorMessage[0] != null && !errorMessage[0].isEmpty())
            model.addAttribute(errorMessage[0]);
        else
            model.addAttribute(Double.toString(ret[0]));
        return model;
    }

    @ExceptionHandler(CalculatorException.class)
    public ModelMap handleCustomException(CalculatorException ex) {
        String message;
        if (ex.getResourceMessage() != null && !ex.getResourceMessage().isEmpty())
            message = messageSource.getMessage(ex.getResourceMessage(), ex.getArgs(), localePl);
        else
            message = ex.getCause().getMessage();

        log.warn(message, ex);
        ModelMap model = new ModelMap(CALCULATOR_HTML);
        model.addAttribute(message);
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
