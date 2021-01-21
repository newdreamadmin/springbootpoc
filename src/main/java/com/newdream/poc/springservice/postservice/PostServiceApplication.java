/************************************************************************
* Defining the PostServiceApplication.
* WHAT:
*     class as Spring Boot Application
*
*
* WHEN         WHO       WHY
* 2021-01-18   Bala      Created
/************************************************************************/
package com.newdream.poc.springservice.postservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PostServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostServiceApplication.class, args);
	}

}
