package pl.sjacek.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for calculations
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data public class CalculateDTO {

    /**
     * The expression
     */
    private String expression;
}
