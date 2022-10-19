package com.example.demo.ProductDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.demo.Product.Product;
import com.example.demo.Website.Website;

@Entity(name = "product_detailss")
public class ProductDetails {

  @Id
  @Column(name = "product_id")
  private String id;

  @ManyToOne
  @JoinColumn(name = "product_id", insertable = false, updatable = false)
  private Product product;

  @ManyToOne
  @JoinColumn(name = "website_id", referencedColumnName = "id")
  private Website website;

  @Column(name = "model_name", nullable = false)
  private String modelName;

  @Column(name = "color", nullable = false)
  private String color;

  @Column(name = "screen_size_inch", nullable = false)
  private int screenSize;

  @Column(name = "display_resolution_px", nullable = false)
  private String displayResolution;

  @Column(name = "response_time_ms", nullable = false)
  private int responseTime;

  @Column(name = "refresh_rate_hz", nullable = false)
  private int refreshRate;

  @Column(name = "connections", nullable = false)
  private String connections;

  @Column(name = "display_type", nullable = false)
  private String displayType;

  @Column(name = "aspect_ratio", nullable = false)
  private String aspectRatio;

  public ProductDetails() {
  }

  public ProductDetails(String id, Product product, Website website, String modelName, String color, int screenSize,
      String displayResolution, int responseTime, int refreshRate, String connections, String displayType,
      String aspectRatio) {
    this.id = id;
    this.product = product;
    this.website = website;
    this.modelName = modelName;
    this.color = color;
    this.screenSize = screenSize;
    this.displayResolution = displayResolution;
    this.responseTime = responseTime;
    this.refreshRate = refreshRate;
    this.connections = connections;
    this.displayType = displayType;
    this.aspectRatio = aspectRatio;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Website getWebsite() {
    return website;
  }

  public void setWebsite(Website website) {
    this.website = website;
  }

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public int getScreenSize() {
    return screenSize;
  }

  public void setScreenSize(int screenSize) {
    this.screenSize = screenSize;
  }

  public String getDisplayResolution() {
    return displayResolution;
  }

  public void setDisplayResolution(String displayResolution) {
    this.displayResolution = displayResolution;
  }

  public int getResponseTime() {
    return responseTime;
  }

  public void setResponseTime(int responseTime) {
    this.responseTime = responseTime;
  }

  public int getRefreshRate() {
    return refreshRate;
  }

  public void setRefreshRate(int refreshRate) {
    this.refreshRate = refreshRate;
  }

  public String getConnections() {
    return connections;
  }

  public void setConnections(String connections) {
    this.connections = connections;
  }

  public String getDisplayType() {
    return displayType;
  }

  public void setDisplayType(String displayType) {
    this.displayType = displayType;
  }

  public String getAspectRatio() {
    return aspectRatio;
  }

  public void setAspectRatio(String aspectRatio) {
    this.aspectRatio = aspectRatio;
  }

}
