package com.price_comparator.Compr;

//Hibernate imports
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Component;

/**
 * Hibernate functions to interact with the database entities.
 */
@Component
public class Hibernate {
    // Creates new Sessions interact with the database
    private SessionFactory sessionFactory;

    /**
     * Empty constructor
     */
    Hibernate() {
    }

    /**
     * Sets up the session factory.
     * Call this method first.
     */
    public void init() {
        try {
            // Create a builder for the standard service registry
            StandardServiceRegistryBuilder standardServiceRegistryBuilder = new StandardServiceRegistryBuilder();

            // Load configuration from hibernate configuration file.
            standardServiceRegistryBuilder.configure("hibernate-annotations.cfg.xml");

            // Create the registry that will be used to build the session factory
            StandardServiceRegistry registry = standardServiceRegistryBuilder.build();
            try {
                // Create the session factory
                sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                /*
                 * The registry would be destroyed by the SessionFactory,
                 */
                System.err.println("Session Factory build failed.");
                e.printStackTrace();
                StandardServiceRegistryBuilder.destroy(registry);
            }

            // Ouput result
            System.out.println("Session factory built.");

        } catch (Throwable ex) {
            System.err.println("SessionFactory creation failed." + ex);
        }
    }

    /**
     * Closes Hibernate down and stops its threads from running
     */
    public void shutDown() {
        sessionFactory.close();
    }

    /**
     * Adds a new website to the database
     *
     * @param website website object
     */
    public void addWebsite(Website website) {
        // Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        // Start transaction
        session.beginTransaction();

        // Add a Website to database
        session.save(website);

        // Commit transaction to save it to database
        session.getTransaction().commit();

        // Close the session and release database connection
        session.close();
        System.out.println("Website " + website.getTitle() + " added to database with ID: " + website.getId());
    }

    /**
     * Get a website from the database
     *
     * @param websiteId id of the website in db
     * 
     * @return website object from db
     */
    public Website getWebsite(Long websiteId) {
        // Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        // Start transaction
        session.beginTransaction();

        // Add a Website to database
        // session.save(website);
        Website website = (Website) session.get(Website.class, websiteId);

        // Close the session and release database connection
        session.close();

        return website;
    }

    /**
     * Adds a new product to the database
     *
     * @param product product object
     */
    public void addProduct(Product product) {
        // Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        // Start transaction
        session.beginTransaction();

        // Add a product to database
        session.save(product);

        // Commit transaction to save it to database
        session.getTransaction().commit();

        // Close the session and release database connection
        session.close();
        System.out.println("Product added to database with ID: " + product.getId());
    }

    /**
     * Get a product from the database
     *
     * @param productId product id in db
     * 
     * @return product object from db
     */
    public Product getProduct(String productId) {
        // Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        // Start transaction
        session.beginTransaction();

        // Add a Website to database
        // session.save(website);
        Product product = (Product) session.get(Product.class, productId);

        // Close the session and release database connection
        session.close();

        return product;
    }

    /**
     * Add product details to the database
     *
     * @param details product detail object
     */
    public void addProductDetails(ProductDetails details) {
        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        session.save(details);

        session.getTransaction().commit();
        session.close();

        System.out.println("Product Details added to database.");

    }

    /**
     * Get a product details from the database
     *
     * @param detailsId detail id in db
     * 
     * @return product details object from db
     */
    public ProductDetails getProductDetails(Long detailsId) {
        // Get a new Session instance from the session factory
        Session session = sessionFactory.getCurrentSession();

        // Start transaction
        session.beginTransaction();

        // Add a Website to database
        // session.save(website);
        ProductDetails details = (ProductDetails) session.get(ProductDetails.class, detailsId);

        // Close the session and release database connection
        session.close();

        return details;
    }
}
