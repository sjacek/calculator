package pl.sjacek.calculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sjacek.calculator.dto.CalculateDTO;
import pl.sjacek.calculator.model.Calculation;
import pl.sjacek.calculator.repositories.CalculationRepository;

import java.util.Date;

/**
 * Created by jacek on 28.05.17.
 */
@Service
public class CalculationService {

    @Autowired
    private CalculationRepository calculationRepository;

    public Calculation save(CalculateDTO param) {
        return calculationRepository.save(Calculation.builder()
                .expression(param.getExpression())
                .datetime(new Date())
                .build());
    }
}
