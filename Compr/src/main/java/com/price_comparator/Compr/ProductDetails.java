package com.price_comparator.Compr;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "product_details")
public class ProductDetails {
    @Id
    @SequenceGenerator(name = "product_details_sequence", sequenceName = "product_details_sequence", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "product_details_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "website_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Website website;

    @Column(name = "screen_size_inch", nullable = true)
    private Double screenSize;

    @Column(name = "display_resolution_px", nullable = true)
    private String displayResolution;

    @Column(name = "refresh_rate_hz", nullable = true)
    private Integer refreshRate;

    public ProductDetails() {
    }

    public ProductDetails(Product product, Website website, Double screenSize, String displayResolution,
            Integer refreshRate) {
        this.product = product;
        this.website = website;
        this.screenSize = screenSize;
        this.displayResolution = displayResolution;
        this.refreshRate = refreshRate;
    }

    public Product getProduct() {
        return product;
    }

    public Website getWebsite() {
        return website;
    }

    public Double getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Double screenSize) {
        this.screenSize = screenSize;
    }

    public String getDisplayResolution() {
        return displayResolution;
    }

    public void setDisplayResolution(String displayResolution) {
        this.displayResolution = displayResolution;
    }

    public Integer getRefreshRate() {
        return refreshRate;
    }

    public void setRefreshRate(Integer refreshRate) {
        this.refreshRate = refreshRate;
    }
}
