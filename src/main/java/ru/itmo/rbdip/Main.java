package ru.itmo.rbdip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
class Main extends SpringBootServletInitializer {

	public SpringApplicationBuilder  configure(SpringApplicationBuilder builder){
		return builder.sources(Main.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(Main.class,args);
	}

}
