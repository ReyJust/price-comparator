package com.price_comparator.Compr;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;

/**
 * Contains main method for application
 */
public class ComprApplication {

	/**
	 * Root function running the app
	 * 
	 * @param args app params
	 */
	public static void main(String[] args) {
		runApplicationAnnotationsConfig();

	}

	/**
	 * Wrap the application with Spring annotation to make use of the framework.
	 */
	static void runApplicationAnnotationsConfig() {

		// Instruct Spring to create and wire beans using annotations.
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		// String userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36
		// (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";

		System.out.println("BEANS:" + Arrays.asList(context.getBeanDefinitionNames()));

		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) context.getBean("taskExecutor");
		Hibernate hibernate = (Hibernate) context.getBean("hibernate");

		hibernate.init();

		AmazonScrapper amazonScrapper = (AmazonScrapper) context.getBean("amazonScrapper");
		NewEggScrapper newEggScrapper = (NewEggScrapper) context.getBean("newEggScrapper");
		ArgosScrapper argosScrapper = (ArgosScrapper) context.getBean("argosScrapper");
		BoxScrapper boxScrapper = (BoxScrapper) context.getBean("boxScrapper");
		FlipKartScrapper flipKartScrapper = (FlipKartScrapper) context.getBean("flipKartScrapper");

		taskExecutor.execute(amazonScrapper);
		taskExecutor.execute(newEggScrapper);
		taskExecutor.execute(argosScrapper);
		taskExecutor.execute(boxScrapper);
		taskExecutor.execute(flipKartScrapper);

		taskExecutor.shutdown();
		hibernate.shutDown();
	}

}
