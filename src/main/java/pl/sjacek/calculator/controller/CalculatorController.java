package pl.sjacek.calculator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.sjacek.calculator.Calculator;
import pl.sjacek.calculator.model.Calculation;
import pl.sjacek.calculator.repositories.CalculationRepository;

import java.lang.invoke.MethodHandles;
import java.text.ParseException;
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



    @PostMapping(path = "/calculate", consumes = APPLICATION_JSON_VALUE)
    public @ResponseBody ModelAndView calculate(@RequestBody CalculateParam param) {
        logger.debug("calculate({})", param.getExpression());

        calculationRepository.save(Calculation.builder()
                .expression(param.getExpression())
                .build());

        List<Calculation> calculations = calculationRepository.findAll();
        calculations.forEach(calculation1 -> logger.info(calculation1.getExpression()) );

        // https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc
        // http://www.baeldung.com/exception-handling-for-rest-with-spring
        ModelAndView response = new ModelAndView();
        try {
            response.addObject("result", Calculator.calculate(param.getExpression()));
        }
        catch (ParseException ex) {

            response.setParseException(ex);
        }

        return response;
    }
}
