package org.example.tmuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TmUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmUserApplication.class, args);
    }

}
