package com.veedo.tsk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TasktrackrApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasktrackrApplication.class, args);
	}

}
