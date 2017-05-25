package pl.sjacek.calculator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import pl.sjacek.calculator.Calculator;
import pl.sjacek.calculator.CalculatorException;
import pl.sjacek.calculator.model.Calculation;
import pl.sjacek.calculator.repositories.CalculationRepository;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by jacek.sztajnke on 2017-05-23.
 */
@RestController
@RequestMapping("/v1/calculator")
public class CalculatorController {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private CalculationRepository calculationRepository;

    @PostMapping(path = "/calculate", consumes = APPLICATION_JSON_VALUE, headers = "Accept=application/json")
    public ModelMap calculate(@RequestBody CalculateParam param) throws CalculatorException {
        logger.debug("calculate({})", param.getExpression());

        calculationRepository.save(Calculation.builder()
                .expression(param.getExpression())
                .datetime(new Date())
                .build());

        ModelMap model = new ModelMap("calculator.html");
        model.addAttribute(Double.toString(Calculator.calculate(param.getExpression())));
        return model;
    }

    @ExceptionHandler(CalculatorException.class)
    public ModelMap handleCustomException(CalculatorException ex) {
        logger.warn(ex.getMessage(), ex);
        ModelMap model = new ModelMap("calculator.html");
        model.addAttribute(ex.getMessage());
        return model;
    }

    @ExceptionHandler(Exception.class)
    public ModelMap handleAllException(Exception ex) {
        logger.warn(ex.getMessage(), ex);
        ModelMap model = new ModelMap("calculator.html");
        StringBuilder message = new StringBuilder();
        message.append(ex.getClass().getName()).append(": ");
        if (ex.getMessage() != null)
            message.append(ex.getMessage());
        model.addAttribute(message.toString());
        return model;
    }
}
