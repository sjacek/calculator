package pl.sjacek.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for e^x integral calculations
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data public class CalculateIntegralDTO {

    private Integer intervalBegin;

    private Integer intervalEnd;

    private Integer threads;

    private Integer repetitions;
}
