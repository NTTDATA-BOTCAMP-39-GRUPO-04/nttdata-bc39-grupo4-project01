package com.nttdata.bc39.grupo04.credit;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.nttdata.bc39.grupo04"})
public class CreditApplication {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        SpringApplication.run(CreditApplication.class, args);
    }

}
