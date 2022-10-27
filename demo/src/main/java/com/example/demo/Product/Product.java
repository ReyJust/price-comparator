package com.example.demo.Product;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.demo.Website.Website;

@Entity(name = "productt")
public class Product {

  @Id
  @Column(name = "product_id", updatable = false)
  private String id;

  @Column(name = "in_stock", nullable = false)
  private boolean inStock;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "url", nullable = false, columnDefinition = "TEXT")
  private String url;

  @Column(name = "brand", nullable = false)
  private String brand;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "website_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Website website;

  @Column(name = "website_customer_rating", nullable = false)
  private int websiteCustomerRating;

  @Column(name = "description", nullable = true)
  private String description;

  @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
  private String imageUrl;

  @Column(name = "image_2_url", nullable = true, columnDefinition = "TEXT")
  private String image2Url;

  @Column(name = "price", nullable = true)
  private double price;

  // @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
  // @PrimaryKeyJoinColumn
  // private ProductDetails productDetails;

  public Product() {
  }

  public Product(String id, boolean inStock, String title, String url, String brand, Website website,
      int websiteCustomerRating, String description, String imageUrl, String image2Url, double price) {
    this.id = id;
    this.inStock = inStock;
    this.title = title;
    this.url = url;
    this.brand = brand;
    this.website = website;
    this.websiteCustomerRating = websiteCustomerRating;
    this.description = description;
    this.imageUrl = imageUrl;
    this.image2Url = image2Url;
    this.price = price;
  }

  public String getId() {
    return id;
  }

  public boolean isInStock() {
    return inStock;
  }

  public void setInStock(boolean inStock) {
    this.inStock = inStock;
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

  public int getWebsiteCustomerRating() {
    return websiteCustomerRating;
  }

  public void setWebsiteCustomerRating(int websiteCustomerRating) {
    this.websiteCustomerRating = websiteCustomerRating;
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

  public String getImage2Url() {
    return image2Url;
  }

  public void setImage2Url(String image2Url) {
    this.image2Url = image2Url;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }
}
