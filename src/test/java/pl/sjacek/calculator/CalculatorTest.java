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
    public void testCalculate() throws CalculatorException {
        assertEquals(25.0d, calculate("5*5"), 0.0d);
        assertEquals(2.0d, calculate("1+1"), 0.0d);
        assertEquals(2.0d, calculate("1+1"), 0.0d);
        assertEquals(2.0d, calculate("1+1"), 0.0d);
    }

    @Test
    public void evaluateSquare() throws CalculatorException {
        assertEquals(9.0d, calculate("square 3"), 0.0d);
    }

    @Test
    public void evaluateRoot() throws CalculatorException {
        assertEquals(3.0d, calculate("root 9"), 0.0d);
    }

    @Test
    public void evaluateZeroOperators() throws CalculatorException {
        assertEquals(1.0d, calculate("1"), 0.0d);
    }

    @Test
    public void evaluateOneOperator() throws CalculatorException {
        assertEquals(2.0d, calculate("1+1"), 0.0d);
    }

    @Test
    public void evaluateOneOperatorWithBraces() throws CalculatorException {
        assertEquals(2.0d, calculate("(1+1)"), 0.0d);
    }

    @Test
    public void evaluateTwoOperatorWithBraces() throws CalculatorException {
        assertEquals(1.0d, calculate("[(1-0)+(1-1)]"), 0.0d);
    }

    @Test(expected=CalculatorException.class)
    public void evaluateTwoOperatorWithWrongBraces() throws CalculatorException {
        assertEquals(1.0d, calculate("[(1-0)+(1-1)}"), 0.0d);
    }

    @Test
    public void evaluateThreeOperatorWithInnerBraces() throws CalculatorException {
        assertEquals(2.0d, calculate("(1-1)+(1+1)"), 0.0d);
    }

    @Test
    public void evaluateThreeOperatorWithNoBraces() throws CalculatorException {
        assertEquals(4.0d, calculate("5-2+1"), 0.0d);
    }

    @Test(expected=CalculatorException.class)
    public void evaluateMissingParens() throws CalculatorException {
        calculate("((1+1)");
    }

    @Test(expected=CalculatorException.class)
    public void evaluateMissingParens2() throws CalculatorException {
        calculate("(1+1))");
    }

    @Test(expected=CalculatorException.class)
    public void evaluateBadNumberFormat() throws CalculatorException {
        calculate("a+1");
    }

    @Test(expected=CalculatorException.class)
    public void evaluateBadNumberFormat2() throws CalculatorException {
        calculate("2,0+1");
    }
}
