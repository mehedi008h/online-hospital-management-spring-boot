package com.online.hospital.managment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OnlineHospitalManagmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineHospitalManagmentApplication.class, args);
    }

}
