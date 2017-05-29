package pl.sjacek.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jacek on 25.05.17.
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
