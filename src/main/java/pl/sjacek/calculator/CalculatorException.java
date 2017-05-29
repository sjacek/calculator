package pl.sjacek.calculator;

import lombok.Getter;

/**
 * Exception used by calculator
 */
@Getter
public class CalculatorException extends Exception {

    private String resourceMessage;

    private String[] args;

    CalculatorException(Exception ex) {
        super(ex);
    }

    CalculatorException(String resourceMeassage, String... args) {
        init(resourceMeassage, args);
    }

    CalculatorException(Exception ex, String resourceMeassage, String... args) {
        super(ex);
        init(resourceMeassage, args);
    }

    private void init(String resourceMeassage, String... args) {
        this.resourceMessage = resourceMeassage;
        this.args = args != null ? args : new String[]{};
    }
}
