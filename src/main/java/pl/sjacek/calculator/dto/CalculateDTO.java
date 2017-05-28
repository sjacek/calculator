package pl.sjacek.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jacek.sztajnke on 2017-05-23.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data public class CalculateDTO {
    private String expression;
}
