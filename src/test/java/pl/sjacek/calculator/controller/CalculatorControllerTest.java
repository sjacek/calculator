package pl.sjacek.calculator.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import pl.sjacek.calculator.config.IntegralCalculationConfig;
import pl.sjacek.calculator.controller.async.IntegralBean;
import pl.sjacek.calculator.dto.CalculateDTO;
import pl.sjacek.calculator.dto.CalculateIntegralDTO;
import pl.sjacek.calculator.service.CalculationService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by jacek on 27.05.17.
 */
public class CalculatorControllerTest {

    private CalculatorController calculatorController;

    private CalculationService calculationServiceMock;

    @MockBean
    private IntegralBean integralBeanMock;

    @Before
    public void setUp() {
        calculatorController = new CalculatorController();

        calculationServiceMock = mock(CalculationService.class);
        ReflectionTestUtils.setField(calculatorController, "calculationService", calculationServiceMock);

        integralBeanMock = mock(IntegralBean.class);
        ReflectionTestUtils.setField(calculatorController, "integralBean", integralBeanMock);
    }

    @Test
    public void calculate() throws Exception {
        CalculateDTO calculateDTO = CalculateDTO.builder()
                .expression("5+3-2")
                .build();
        assertEquals("6.0", calculatorController.calculate(calculateDTO).get("string"));
    }
}
