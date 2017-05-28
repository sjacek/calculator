package pl.sjacek.calculator.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jacek on 27.05.17.
 */
public class ErrorControllerTest {

    private ErrorController errorController;

    @Before
    public void setUp() {
        errorController = new ErrorController();
    }

    @Test
    public void show404Page() {
        String view = errorController.show404Page();
        assertEquals(ErrorController.VIEW_NOT_FOUND, view);
    }

    @Test
    public void showInternalServerErrorPage() {
        String view = errorController.showInternalServerErrorPage();
        assertEquals(ErrorController.VIEW_INTERNAL_SERVER_ERROR, view);
    }
}