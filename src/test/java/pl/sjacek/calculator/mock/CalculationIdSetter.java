package pl.sjacek.calculator.mock;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import pl.sjacek.calculator.model.Calculation;

import static org.junit.Assert.assertEquals;

/**
 * Created by jacek on 27.05.17.
 */
public class CalculationIdSetter implements Answer<Void> {

    private Long id;

    public CalculationIdSetter(Long id) {
        this.id = id;
    }

    @Override
    public Void answer(InvocationOnMock invocation) throws Throwable {
        assertEquals(1, invocation.getArguments().length);
        Calculation calculation = (Calculation) invocation.getArguments()[0];
        calculation.setId(id);
        return null;
    }
}
