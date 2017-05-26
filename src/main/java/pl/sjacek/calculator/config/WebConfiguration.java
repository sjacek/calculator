package pl.sjacek.calculator.config;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by jacek on 22.05.17.
 */
@Configuration
@Profile({"default", "local"})
public class WebConfiguration {
    @Bean
    ServletRegistrationBean h2servletRegistration(@Value("${spring.h2.console.path}") String h2ConsolePath){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings(h2ConsolePath);
        return registrationBean;
    }
}
