/*
package br.edu.ufrgs.inf.bpm.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestApplication {

    private static ConsumerController consumerController;

    public static ConsumerController getConsumerController(){
        if(consumerController == null) {
            ApplicationContext ctx = SpringApplication.run(RestApplication.class);
            consumerController = ctx.getBean(ConsumerController.class);
        }

        return consumerController;
    }

    @Bean
    public ConsumerController consumerController() {
        return new ConsumerController();
    }

}
*/