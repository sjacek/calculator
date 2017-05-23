package pl.sjacek.calculator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/home")
	public String home() {
		return "index";
	}

	@RequestMapping("/")
	public String calculator() {
		return "calculator";
	}
}
