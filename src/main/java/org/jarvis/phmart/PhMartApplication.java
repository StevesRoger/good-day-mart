package org.jarvis.phmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootApplication
public class PhMartApplication  {

    public static void main(String[] args) {
        SpringApplication.run(PhMartApplication.class, args);
    }

}

