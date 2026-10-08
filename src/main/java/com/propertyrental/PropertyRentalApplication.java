package com.propertyrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PropertyRentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyRentalApplication.class, args);
        System.out.println("==========================================================");
        System.out.println("   PROPERTY RENTAL SYSTEM BACKEND STARTED SUCCESSFULLY!   ");
        System.out.println("   API Base URL: http://localhost:8080/api               ");
        System.out.println("==========================================================");
    }
}
