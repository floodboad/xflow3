package org.xsnake.cloud.xflow3;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.xsnake.cloud.common.service.ExceptionResolver;
import org.xsnake.cloud.common.service.JsonMessageConverter;

@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@ComponentScan(basePackages="org.xsnake")
public class Application {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class).web(true).run(args);
	}
	
	@Bean
	public JsonMessageConverter jsonMessageConverter(){
		return new JsonMessageConverter();
	}
	
	@Bean 
	public ExceptionResolver exceptionResolver(){
		return new ExceptionResolver();
	}
	
}
