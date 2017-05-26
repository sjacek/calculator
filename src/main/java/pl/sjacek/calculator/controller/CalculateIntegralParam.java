package pl.sjacek.calculator.controller;

import lombok.Data;

/**
 * Created by jacek on 25.05.17.
 */
@Data class CalculateIntegralParam {

    private Integer interval;

    private Integer threads;

    private Integer repetitions;
}
