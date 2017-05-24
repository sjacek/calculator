package pl.sjacek.calculator;

/**
 * Created by jacek.sztajnke on 2017-05-24.
 */
public class CalculatorException extends Exception {
    CalculatorException(Exception ex) {
        super(ex);
    }
}
