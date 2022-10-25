package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.example.demo.Product.Product;
import com.example.demo.Product.ProductRepository;
import com.example.demo.ProductDetails.ProductDetails;
import com.example.demo.ProductDetails.ProductDetailsRepository;
import com.example.demo.Website.Website;
import com.example.demo.Website.WebsiteRepository;

import com.example.demo.Scrapper.Amazon;
import com.example.demo.Scrapper.BestBuy;
import com.example.demo.Scrapper.Dx;
import com.example.demo.Scrapper.Ebay;
import com.example.demo.Scrapper.NewEgg;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(WebsiteRepository websiteRepository,
			ProductRepository productRepository,
			ProductDetailsRepository productDetailsRepository) {
		return args -> {

			Amazon amazon_scrapper = new Amazon();
			Ebay ebay_scrapper = new Ebay();
			Dx dx_scrapper = new Dx();
			NewEgg newegg_scrapper = new NewEgg();
			BestBuy bestbuy_scrapper = new BestBuy();

			ApplicationContext context = new AnnotationConfigApplicationContext(AsyncConfig.class);
			ThreadPoolTaskExecutor taskExecutor = context.getBean(ThreadPoolTaskExecutor.class);

			taskExecutor.execute(amazon_scrapper);
			// taskExecutor.execute(ebay_scrapper);
			// taskExecutor.execute(dx_scrapper);
			// taskExecutor.execute(newegg_scrapper);
			// taskExecutor.execute(bestbuy_scrapper);

			taskExecutor.shutdown();

			// Website ebay = new Website("ebay", "ebay", "ebay");
			// websiteRepository.save(ebay);

			// Product p1 = new Product("product_1", true, "test", "test", "test", ebay, 0,
			// "test", "test", "test", 112);

			// ProductDetails pd1 = new ProductDetails("product_1", p1, ebay, "model",
			// "color", 22, "1600x900", 4, 75, "HDMI",
			// "HD", "16:9");

			// productRepository.save(p1);

			// System.out.println(pd1.getModelName());

			// productDetailsRepository.save(pd1);

		};
	}
}
