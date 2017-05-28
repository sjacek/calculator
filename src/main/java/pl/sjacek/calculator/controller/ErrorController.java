package pl.sjacek.calculator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by jacek on 27.05.17.
 */
@Controller
@Slf4j
public class ErrorController {

    public static final String VIEW_INTERNAL_SERVER_ERROR = "error/error";
    public static final String VIEW_NOT_FOUND = "error/404";

    @RequestMapping(value = "/error/404", method = RequestMethod.GET)
    public String show404Page() {
        log.debug("Rendering 404 page");
        return VIEW_NOT_FOUND;
    }

    @RequestMapping(value = "/error/error", method = RequestMethod.GET)
    public String showInternalServerErrorPage() {
        log.debug("Rendering internal server error page");
        return VIEW_INTERNAL_SERVER_ERROR;
    }
}
