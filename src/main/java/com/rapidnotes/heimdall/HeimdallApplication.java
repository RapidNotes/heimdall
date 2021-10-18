package com.rapidnotes.heimdall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication()
public class HeimdallApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(HeimdallApplication.class, args);
	}

}
