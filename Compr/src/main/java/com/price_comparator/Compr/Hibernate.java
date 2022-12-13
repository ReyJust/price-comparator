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
     * @param website
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
     * Adds a new product to the database
     *
     * @param product
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
     * Add product details to the database
     *
     * @param details
     */
    public void addProductDetails(ProductDetails details) {
        Session session = sessionFactory.getCurrentSession();

        session.beginTransaction();

        session.save(details);

        session.getTransaction().commit();
        session.close();

        System.out.println("Product Details added to database.");

    }
}
