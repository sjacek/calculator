package pl.sjacek.calculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static pl.sjacek.calculator.Calculator1.RPNtoDouble;
import static pl.sjacek.calculator.Calculator1.expToRPN;

/**
 * Created by jacek.sztajnke on 2017-05-23.
 */
class CalculatorTest {

    @Test
    void calculateTest() {
        String[] input = "( 1 + 2 ) * ( 3 / 4 ) - ( 5 + 6 )".split(" ");
        String[] output = expToRPN(input);

        // Build output RPN string minus the commas
        System.out.print("Stack: ");
        for (String token : output) {
            System.out.print("[ ");
            System.out.print(token + " ");
            System.out.print("]");
        }
        System.out.println(" ");
        // Feed the RPN string to RPNtoDouble to give result
        Double result = RPNtoDouble( output );
        System.out.println("Answer= " + result);
        assertEquals(2,2);
    }
}