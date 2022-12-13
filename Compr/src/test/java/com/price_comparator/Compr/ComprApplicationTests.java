package com.price_comparator.Compr;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//JUnit 5 imports
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

@DisplayName("Test Compr")
public class ComprApplicationTests {

	@BeforeAll
	static void initAll() {
	}

	@BeforeEach
	void init() {
	}

	/**
	 * Test that we create a website object properly.
	 */
	@Test
	@DisplayName("create website")
	public void createWebsite() {

		String title = "myTestWebsite";
		String image_url = "https://example_image.com";
		String url = "https://example.com";

		Website myTestWebsite = new Website(title, image_url, url);

		// Verify that the Website object is properly created will all its attributes
		assertEquals(myTestWebsite.getTitle(), title);
		assertEquals(myTestWebsite.getUrl(), url);
		assertEquals(myTestWebsite.getWebsiteImageUrl(), image_url);
		assertEquals(myTestWebsite.getClass(), Website.class);

	}

	/**
	 * Test that we can save a website to the database without error.
	 */
	@Test
	@DisplayName("save website")
	public void saveWebsite() {

		String title = "myTestWebsite";
		String image_url = "https://example_image.com";
		String url = "https://example.com";

		Website myTestWebsite = new Website(title, image_url, url);

		Hibernate hibernate = new Hibernate();
		hibernate.init();

		hibernate.addWebsite(myTestWebsite);

		// Get the element back from the db
		Website persistent_web = hibernate.getWebsite(myTestWebsite.getId());
		hibernate.shutDown();

		assertEquals(persistent_web.getTitle(), title);

	}

	/**
	 * Test that we can save a product to the database without error.
	 */
	@Test
	@DisplayName("save product")
	public void saveProduct() {
		String title = "myTestWebsite";
		String image_url = "https://example_image.com";
		String url = "https://example.com";

		Website myTestWebsite = new Website(title, image_url, url);

		Hibernate hibernate = new Hibernate();
		hibernate.init();

		hibernate.addWebsite(myTestWebsite);
		Product product = new Product("productId", "model", "title",
				myTestWebsite.getUrl() + "link.com", "brand", myTestWebsite,
				"", "image", 0.0);
		hibernate.addProduct(product);

		assertEquals(product.getClass(), Product.class);

		// Get the element back from the db
		Product persistent_product = hibernate.getProduct(product.getId());
		hibernate.shutDown();

		assertEquals(persistent_product.getId(), product.getId());

		hibernate.shutDown();
	}

	/**
	 * Test that we can save a product details to the database without error.
	 */
	@Test
	@DisplayName("Save product details")
	public void saveProductDetails() {
		String title = "myTestWebsite";
		String image_url = "https://example_image.com";
		String url = "https://example.com";

		Website myTestWebsite = new Website(title, image_url, url);

		Hibernate hibernate = new Hibernate();
		hibernate.init();

		hibernate.addWebsite(myTestWebsite);
		Product product = new Product("productId", "model", "title",
				myTestWebsite.getUrl() + "link.com", "brand", myTestWebsite,
				"", "image", 0.0);
		hibernate.addProduct(product);

		ProductDetails details = new ProductDetails(product, myTestWebsite, 0.0, "displayResolution",
				0);

		assertEquals(details.getClass(), ProductDetails.class);

		hibernate.shutDown();
	}

	/**
	 * Test a scrapper thread creation.
	 */
	@Test
	@DisplayName("Save product details")
	public void createScrapperThread() {
		System.out.println(NewEggScrapper.class.isAssignableFrom(Thread.class));
	}

	@AfterAll
	static void tearDownAll() {
	}
}
