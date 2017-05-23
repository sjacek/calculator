package pl.sjacek.calculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.sjacek.calculator.repositories.CalculationRepository;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by jacek on 23.05.17.
 */
@Controller
@RequestMapping("/v1/calculator")
public class CalculatorService {

    @Autowired
    private CalculationRepository calculationRepository;

    @RequestMapping(path="/calculate", method = GET)
    public @ResponseBody Double calculate(@RequestParam(value = "expression") String expression) {
        Double result = 2.0;
        return result;
    }
}
