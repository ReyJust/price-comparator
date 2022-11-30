package com.price_comparator.Compr;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "product")
public class Product {
    @Id
    @SequenceGenerator(name = "website_sequence", sequenceName =
            "website_sequence", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "website_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    private String title;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(name = "brand", nullable = false)
    private String brand;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
//    @JoinColumn(name = "website_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne()
    @JoinColumn(name ="website_id")
    private Website website;

//    @Column(name = "website_id", nullable = false)
//    private int websiteId;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "price", nullable = true)
    private double price;

    // @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    // @PrimaryKeyJoinColumn
    // private ProductDetails productDetails;

    public Product() {
    }

    public Product(String model, String title, String url, String brand,
                             Website website, String description, String imageUrl, double price) {
        this.model = model;
        this.title = title;
        this.url = url;
        this.brand = brand;
        this.website = website;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}