package pl.sjacek.calculator;

import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static pl.sjacek.calculator.Calculator.calculate;

/**
 * Created by jacek.sztajnke on 2017-05-23.
 */
public class CalculatorTest {

    @Test
    public void testCalculate() throws ParseException {
        assertEquals(25.0d, calculate("5*5"), 0.0d);
        assertEquals(2.0d, calculate("1+1"), 0.0d);
        assertEquals(2.0d, calculate("1+1"), 0.0d);
        assertEquals(2.0d, calculate("1+1"), 0.0d);
    }

    @Test
    public void evaluateZeroOperators() throws ParseException {
        assertEquals(1.0d, calculate("1"), 0.0d);
    }

    @Test
    public void evaluateOneOperator() throws ParseException {
        assertEquals(2.0d, calculate("1+1"), 0.0d);
    }

    @Test
    public void evaluateOneOperatorWithParens() throws ParseException {
        assertEquals(2.0d, calculate("(1+1)"), 0.0d);
    }

    @Test
    public void evaluateTwoOperatorWithParens() throws ParseException {
        assertEquals(1.0d, calculate("((1-0)+(1-1))"), 0.0d);
    }

    @Test
    public void evaluateThreeOperatorWithInnerParens() throws ParseException {
        assertEquals(2.0d, calculate("(1-1)+(1+1)"), 0.0d);
    }

    @Test
    public void evaluateThreeOperatorWithNoParens() throws ParseException {
        assertEquals(4.0d, calculate("5-2+1"), 0.0d);
    }

    @Test(expected=ParseException.class)
    public void evaluateMissingParens() throws ParseException {
        calculate("((1+1)");
    }

    @Test(expected=ParseException.class)
    public void evaluateMissingParens2() throws ParseException {
        calculate("(1+1))");
    }
}
