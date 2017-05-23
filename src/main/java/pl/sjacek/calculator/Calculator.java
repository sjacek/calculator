package pl.sjacek.calculator;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Vladimir on 20.02.14.
 */
public class Calculator {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final List<Character> DIVIDERS = ImmutableList.of('*', '/', '-', '+');

//    private static final ArrayList<Character> DIVIDERS = new ArrayList<>(Arrays.asList('*', '/', '-', '+'));
    private static final int RIGHT_DIRECTION = 1;
    private static final int LEFT_DIRECTION = -1;

    private String expression;

    public Calculator(String expression) {
        this.expression = expression;
    }

    public double calculate() throws ParseException {
        expression = prepareExpression();
        logger.info("Prepared expression: " + expression);
        return Double.parseDouble(recursiveCalculate(expression));
    }

    public static double calculate(String expression) throws ParseException {
        return new Calculator(expression).calculate();
    }

    //Recursive function with the state machine
    //states "(", "sin", "cos", "exp", "*", "/", "+", "-"
    private String recursiveCalculate(String expression) throws ParseException {
        int pos;
        logger.info("Solving expression: " + expression);
        //Extracting expression from braces, doing recursive call
        //replace braced expression on result of it solving
        if (-1 != (pos = expression.indexOf("("))) {

            String subexp = extractExpressionFromBraces(pos);
            expression = expression.replace("(" + subexp + ")", recursiveCalculate(subexp));

            return recursiveCalculate(expression);

            //Three states for calculating sin cos exp
            //input must be like sin0.7
        } else if (-1 != (pos = expression.indexOf("sin"))) {

            pos += 2;//shift index to last symbol of "sin" instead of first

            String number = extractNumber(pos, RIGHT_DIRECTION);

            expression = expression.replace("sin" + number, Double.toString(Math.sin(Double.parseDouble(number))));

            return recursiveCalculate(expression);

        } else if (-1 != (pos = expression.indexOf("cos"))) {

            pos += 2;

            String number = extractNumber(pos, RIGHT_DIRECTION);

            expression = expression.replace("cos" + number, Double.toString(Math.cos(Double.parseDouble(number))));

            return recursiveCalculate(expression);

        } else if (-1 != (pos = expression.indexOf("exp"))) {

            pos += 2;

            String number = extractNumber(pos, RIGHT_DIRECTION);

            expression = expression.replace("exp" + number, Double.toString(Math.exp(Double.parseDouble(number))));

            return recursiveCalculate(expression);


        } else if (expression.indexOf("*") > 0 | expression.indexOf("/") > 0) {

            int multPos = expression.indexOf("*");
            int divPos = expression.indexOf("/");

            pos = Math.min(multPos, divPos);
            if (multPos < 0) pos = divPos;
            else if (divPos < 0) pos = multPos; //If one value of *Pos will be -1 result of min will be incorrect.

            char divider = expression.charAt(pos);

            String leftNum = extractNumber(pos, LEFT_DIRECTION);
            String rightNum = extractNumber(pos, RIGHT_DIRECTION);

            expression = expression.replace(leftNum + divider + rightNum, calcShortExpr(leftNum, rightNum, divider));

            return recursiveCalculate(expression);

        } else if (expression.indexOf("+") > 0 | expression.indexOf("-") > 0) {

            int summPos = expression.indexOf("+");
            int minusPos = expression.indexOf("-");

            pos = Math.min(summPos, minusPos);

            if (summPos < 0) pos = minusPos;
            else if (minusPos < 0) pos = summPos;

            char divider = expression.charAt(pos);

            String leftNum = extractNumber(pos, LEFT_DIRECTION);
            String rightNum = extractNumber(pos, RIGHT_DIRECTION);

            expression = expression.replace(leftNum + divider + rightNum, calcShortExpr(leftNum, rightNum, divider));

            return recursiveCalculate(expression);

        } else return expression;
    }

    private String extractExpressionFromBraces(int pos) throws ParseException {
        int braceDepth = 1;
        StringBuilder subexp = new StringBuilder();

        int i;
        for (i = pos + 1; i < expression.length(); i++) {
            switch (expression.charAt(i)) {
                case '(':
                    braceDepth++;
                    subexp.append("(");
                    break;
                case ')':
                    braceDepth--;
                    if (braceDepth != 0) subexp.append(")");
                    break;
                default:
                    if (braceDepth > 0) subexp.append(expression.charAt(i));

            }
            if (braceDepth == 0 && !subexp.toString().equals(""))
                return subexp.toString();
        }
        throw new ParseException("Failure", i);
    }

    private String extractNumber(int pos, int direction) {

        StringBuilder resultNumber = new StringBuilder();
        int currPos = pos + direction; //shift pos on next symbol from divider

        //For negative numbers
        if (expression.charAt(currPos) == '-') {
            resultNumber.append(expression.charAt(currPos));
            currPos += direction;
        }

        for (; currPos >= 0 &&
                currPos < expression.length() &&
                !DIVIDERS.contains(expression.charAt(currPos));
             currPos += direction) {
            resultNumber.append(expression.charAt(currPos));
        }

        if (direction == LEFT_DIRECTION)
            resultNumber = resultNumber.reverse();

        return resultNumber.toString();
    }

    private String calcShortExpr(String leftNum, String rightNum, char divider) {
        switch (divider) {
            case '*':
                return Double.toString(Double.parseDouble(leftNum) * Double.parseDouble(rightNum));
            case '/':
                return Double.toString(Double.parseDouble(leftNum) / Double.parseDouble(rightNum));
            case '+':
                return Double.toString(Double.parseDouble(leftNum) + Double.parseDouble(rightNum));
            case '-':
                return Double.toString(Double.parseDouble(leftNum) - Double.parseDouble(rightNum));
            default:
                return "0";
        }
    }

    private String prepareExpression() {

        expression = expression.replace("PI", Double.toString(Math.PI));
        expression = expression.replace("E", Double.toString(Math.E));
        expression = expression.replace(" ", "");

        return expression;
    }

}