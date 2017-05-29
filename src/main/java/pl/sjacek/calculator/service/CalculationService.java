package pl.sjacek.calculator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sjacek.calculator.dto.CalculateDTO;
import pl.sjacek.calculator.model.Calculation;
import pl.sjacek.calculator.repositories.CalculationRepository;

import java.util.Date;

/**
 * Service class for calculations
 */
@Service
public class CalculationService {

    @Autowired
    private CalculationRepository calculationRepository;

    /**
     * Saves the calculation in the database
     * @param param the calculation data
     * @return the calculation entity with correct id
     */
    public Calculation save(CalculateDTO param) {
        return calculationRepository.save(Calculation.builder()
                .expression(param.getExpression())
                .datetime(new Date())
                .build());
    }
}
