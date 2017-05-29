package pl.sjacek.calculator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home controller
 */
@Controller
public class HomeController {

	@RequestMapping("/")
	public String calculator() {
		return "calculator";
	}
}
