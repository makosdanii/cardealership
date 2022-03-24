package com.makosdanii.cardealership;

import javax.faces.webapp.FacesServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CardealershipApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CardealershipApplication.class, args);

    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        FacesServlet servlet = new FacesServlet();
        ServletRegistrationBean servletRegistrationBean
                = new ServletRegistrationBean(servlet,
                        "/cardealership/*");
        return servletRegistrationBean;
    }
}
