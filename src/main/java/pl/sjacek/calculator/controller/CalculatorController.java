package pl.sjacek.calculator.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.sjacek.calculator.Calculator;
import pl.sjacek.calculator.model.Calculation;
import pl.sjacek.calculator.repositories.CalculationRepository;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Created by jacek.sztajnke on 2017-05-23.
 */
@RestController
@RequestMapping("/v1/calculator")
public class CalculatorController {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private CalculationRepository calculationRepository;

    @PostMapping(path = "/calculate")
    public @ResponseBody Double calculate(@RequestBody CalculateParam param) {
        logger.debug("calculate({})", param.getExpression());
        Double result = 2.0;

        calculationRepository.save(Calculation.builder()
                .expression(param.getExpression())
                .build());

        List<Calculation> calculations = calculationRepository.findAll();
        calculations.forEach(calculation1 -> logger.info(calculation1.getExpression()) );

        return Calculator.calculate(param.getExpression());
    }
}
