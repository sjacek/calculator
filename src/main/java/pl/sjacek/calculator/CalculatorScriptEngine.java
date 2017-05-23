package pl.sjacek.calculator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by jacek.sztajnke on 2017-05-23.
 */
public class CalculatorScriptEngine {
    public static Double calculate(String expression) {
        final ScriptEngineManager engineManager = new ScriptEngineManager();
        final ScriptEngine engine = engineManager.getEngineByName("JavaScript");
        Object o = null;
        try {
            o = engine.eval(expression);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return (Double)o;
    }
}
