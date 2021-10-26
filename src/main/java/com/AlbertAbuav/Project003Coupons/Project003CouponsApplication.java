package com.AlbertAbuav.Project003Coupons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication  // ==> @Configuration + @ComponentScan(basePackages = "com.AlbertAbuav.Project002Coupons") + @ConfigurationScan
@EnableScheduling  // ==> Will execute the "DailyTask"
//@EnableAspectJAutoProxy
public class Project003CouponsApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Project003CouponsApplication.class, args);
		System.out.println("Spring IoC container was loaded!");
	}

}
