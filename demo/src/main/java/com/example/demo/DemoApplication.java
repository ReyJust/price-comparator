package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.classes.Test;
import com.example.demo.Product.Product;
import com.example.demo.Product.ProductRepository;
import com.example.demo.ProductDetails.ProductDetails;
import com.example.demo.ProductDetails.ProductDetailsRepository;
import com.example.demo.Website.Website;
import com.example.demo.Website.WebsiteRepository;

// import org.springframework.scheduling.annotation.EnableAsync;
// import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
// import java.util.concurrent.Executor;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		Test test = new Test();

		test.helloWorld();
	}

	@Bean
	CommandLineRunner commandLineRunner(WebsiteRepository websiteRepository, ProductRepository productRepository,
			ProductDetailsRepository productDetailsRepository) {
		return args -> {
			Website ebay = new Website("ebay", "ebay", "ebay");
			websiteRepository.save(ebay);

			Product p1 = new Product("product_1", true, "test", "test", "test", ebay, 0,
					"test", "test", "test", 112);

			ProductDetails pd1 = new ProductDetails("product_1", p1, ebay, "model", "color", 22, "1600x900", 4, 75, "HDMI",
					"HD", "16:9");

			productRepository.save(p1);

			System.out.println(pd1.getModelName());

			productDetailsRepository.save(pd1);

		};
	}

	// @Bean (name = "taskExecutor")
	// public Executor taskExecutor() {
	// LOGGER.debug("Creating Async Task Executor");
	// final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	// executor.setCorePoolSize(2);
	// executor.setMaxPoolSize(2);
	// executor.setQueueCapacity(100);
	// executor.setThreadNamePrefix("CarThread-");
	// executor.initialize()
	// return executor;
	// }
}
