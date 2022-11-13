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
import com.example.demo.Scrapper.NewEgg;
import com.example.demo.Scrapper.Argos;
import com.example.demo.Scrapper.Flipkart;
// import com.example.demo.Scrapper.Ebay;

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

			String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";

			Website amazon = new Website("Amazon", "https://upload.wikimedia.org/wikipedia/commons/a/a9/Amazon_logo.svg",
					"https://www.amazon.com");
			Amazon amazonScrapper = new Amazon(amazon, userAgent);

			Website newEgg = new Website("NewEgg",
					"https://c1.neweggimages.com/WebResource/Themes/Nest/logos/Newegg_full_color_logo_RGB.SVG",
					"https://www.newegg.com");
			NewEgg newEggScrapper = new NewEgg(newEgg, userAgent);

			Website argos = new Website("Argos",
					"https://media.4rgos.it/i/Argos/logo_argos2x?w=120&h=103&qlt=75&fmt=png",
					"https://www.argos.co.uk");
			Argos argosScrapper = new Argos(argos, userAgent);

			Website flipkart = new Website("Flipkart",
					"https://1000logos.net/wp-content/uploads/2021/02/Flipkart-logo.png",
					"https://www.flipkart.com");
			Flipkart flipkartScrapper = new Flipkart(flipkart, userAgent);

			// Ebay ebay_scrapper = new Ebay();
			// Dx dx_scrapper = new Dx();
			// BestBuy bestbuy_scrapper = new BestBuy();

			ApplicationContext context = new AnnotationConfigApplicationContext(AsyncConfig.class);
			ThreadPoolTaskExecutor taskExecutor = context.getBean(ThreadPoolTaskExecutor.class);

			// taskExecutor.execute(amazonScrapper);
			// taskExecutor.execute(newEggScrapper);
			// taskExecutor.execute(argosScrapper);
			taskExecutor.execute(flipkartScrapper);

			// taskExecutor.execute(dx_scrapper);

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
