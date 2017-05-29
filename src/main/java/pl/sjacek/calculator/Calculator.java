package pl.sjacek.calculator;

import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.containsAny;

/**
 * Calculator class
 */
@Slf4j
public class Calculator {

    private static final List<Character> DIVIDERS = ImmutableList.of('*', '/', '-', '+');

    private enum Direction {
        Right(1),
        Left(-1);

        private int shift;

        Direction(int shift) {
            this.shift = shift;
        }

        public int getShift() {
            return shift;
        }
    }

    private static final String SQUARE = "square";
    private static final String ROOT = "root";
    private static final String EXP = "exp";

    private String expression;

    /**
     * Class constructor
     * @param expression expression to resolve
     */
    public Calculator(String expression) {
        this.expression = expression;
    }

    public double calculate() throws CalculatorException {
        expression = prepareExpression();
        log.trace("Prepared expression: " + expression);
        return Double.parseDouble(recursiveCalculate(expression));
    }

    /**
     * Static method for invoking calculations
     * @param expression expression to resolve
     * @return result of the calculation
     * @throws CalculatorException
     */
    public static double calculate(String expression) throws CalculatorException {
        return new Calculator(expression).calculate();
    }

    /**
     * Recursive function with the state machine states "(", "square", "root", "exp", "*", "/", "+", "-"
     * @param expression expression to resolve
     * @return result of the calculation
     * @throws CalculatorException
     */
    private String recursiveCalculate(String expression) throws CalculatorException {
        int pos;
        log.trace("Solving expression: " + expression);
        //Extracting expression from braces, doing recursive call
        //replace braced expression on result of it solving
        if (containsAny(expression, "{[(")) {
            char bracket1 = 0;
            char bracket2 = 0;
            pos = expression.indexOf("{");
            if (pos != -1) {
                bracket1 = '{';
                bracket2 = '}';
            }
            else {
                pos = expression.indexOf("[");
                if (pos != -1) {
                    bracket1 = '[';
                    bracket2 = ']';
                }
                else {
                    pos = expression.indexOf("(");
                    if (pos != -1) {
                        bracket1 = '(';
                        bracket2 = ')';
                    }
                    else
                        // this should never riched
                        assert false;
                }
            }

            String subexp = extractExpressionFromBraces(expression, pos, bracket1, bracket2);
            expression = expression.replace(bracket1 + subexp + bracket2, recursiveCalculate(subexp));

            int open = expression.indexOf(bracket1);
            int close = expression.indexOf(bracket2);
            if (open == -1 && close != -1)
                throw new CalculatorException("calculator.exception.bad_brackets", Integer.toString(close));

            return recursiveCalculate(expression);
        }
        //Three states for calculating square root exp
        //input must be like square5
        else if (-1 != (pos = expression.indexOf(SQUARE))) {
            pos += 5;//shift index to last symbol of "square" instead of first
            String number = extractNumber(expression, pos, Direction.Right);
            expression = expression.replace(SQUARE + number,
                    Double.toString(Double.parseDouble(number) * Double.parseDouble(number)));

            return recursiveCalculate(expression);
        }
        else if (-1 != (pos = expression.indexOf(ROOT))) {
            pos += 3;
            String sNumber = extractNumber(expression, pos, Direction.Right);
            double number = Double.parseDouble(sNumber);
            if (number < 0)
                throw new CalculatorException("calculator.exception.root_of_negative", Double.toString(number));
            expression = expression.replace(ROOT + sNumber, Double.toString(Math.sqrt(number)));

            return recursiveCalculate(expression);
        }
        else if (-1 != (pos = expression.indexOf(EXP))) {
            pos += 2;
            String number = extractNumber(expression, pos, Direction.Right);
            expression = expression.replace(EXP + number,
                    Double.toString(Math.exp(Double.parseDouble(number))));

            return recursiveCalculate(expression);
        }
        else if (containsAny(expression, "*/")) {
            int multPos = expression.indexOf("*");
            int divPos = expression.indexOf("/");

            pos = Math.min(multPos, divPos);
            if (multPos < 0) pos = divPos;
            else if (divPos < 0) pos = multPos; //If one value of *Pos will be -1 result of min will be incorrect.

            char divider = expression.charAt(pos);

            String leftNum = extractNumber(expression, pos, Direction.Left);
            String rightNum = extractNumber(expression, pos, Direction.Right);
            expression = expression.replace(leftNum + divider + rightNum, calcShortExpr(leftNum, rightNum, divider));

            return recursiveCalculate(expression);
        }
        else if (containsAny(expression, "+-")) {
            int summPos = expression.indexOf("+");
            int minusPos = expression.indexOf("-");

            pos = Math.min(summPos, minusPos);

            if (summPos < 0) pos = minusPos;
            else if (minusPos < 0) pos = summPos;

            char divider = expression.charAt(pos);

            String leftNum = extractNumber(expression, pos, Direction.Left);
            String rightNum = extractNumber(expression, pos, Direction.Right);
            expression = expression.replace(leftNum + divider + rightNum, calcShortExpr(leftNum, rightNum, divider));

            return recursiveCalculate(expression);
        } else return expression;
    }

    /**
     * Function extracting expression from brackets
     * @param expression expression to resolve
     * @param pos position, where to analize expression
     * @param bracket1 opening bracket
     * @param bracket2 closing bracket
     * @return expression from brackets
     * @throws CalculatorException
     */
    private String extractExpressionFromBraces(String expression, int pos, char bracket1, char bracket2) throws CalculatorException {
        int braceDepth = 1;
        StringBuilder subexp = new StringBuilder();

        int i;
        for (i = pos + 1; i < expression.length(); i++) {
            if (expression.charAt(i) == bracket1) {
                braceDepth++;
                subexp.append(bracket1);
            }
            else if (expression.charAt(i) == bracket2) {
                braceDepth--;
                if (braceDepth != 0) subexp.append(bracket2);
            }
            else if (braceDepth > 0)
                subexp.append(expression.charAt(i));

            if (braceDepth == 0 && !subexp.toString().equals(""))
                return subexp.toString();
        }
        throw new CalculatorException("calculator.exception.bad_brackets", Integer.toString(pos));
    }

    /**
     * Get number from the given position
     * @param expression the expression
     * @param pos the given position
     * @param direction direction - right or left
     * @return
     */
    private String extractNumber(String expression, int pos, Direction direction) {

        StringBuilder resultNumber = new StringBuilder();
        int currPos = pos + direction.getShift();

        //For negative numbers
        if (expression.charAt(currPos) == '-') {
            resultNumber.append(expression.charAt(currPos));
            currPos += direction.getShift();
        }

        for (; currPos >= 0 && currPos < expression.length() && !DIVIDERS.contains(expression.charAt(currPos));
             currPos += direction.getShift()) {
            resultNumber.append(expression.charAt(currPos));
        }

        if (direction == Direction.Left)
            resultNumber = resultNumber.reverse();

        return resultNumber.toString();
    }

    /**
     * Calculates simple mathematical operation
     * @param sLeft   left number
     * @param sRight  right number
     * @param divider mathematical operator
     * @return result of the operation
     * @throws CalculatorException
     */
    private String calcShortExpr(String sLeft, String sRight, char divider) throws CalculatorException {

        double left, right;
        try {
            left = Double.parseDouble(sLeft);
        }
        catch (NumberFormatException ex) {
            throw new CalculatorException("calculator.exception.bad_number_format", ex.getMessage());
        }
        try {
            right = Double.parseDouble(sRight);
        }
        catch (NumberFormatException ex) {
            throw new CalculatorException("calculator.exception.bad_number_format", ex.getMessage());
        }

        double result;
        switch (divider) {
            case '*': result = left * right; break;
            case '/':
                if (right == 0)
                    throw new CalculatorException("calculator.exception.divide_by_zero");
                result = left / right;
                break;
            case '+': result = left + right; break;
            case '-': result = left - right; break;
            default:  result = 0.0d;
        }

        return Double.toString(result);
    }

    private String prepareExpression() {
        return expression.toLowerCase().replace(" ", "");
    }

}