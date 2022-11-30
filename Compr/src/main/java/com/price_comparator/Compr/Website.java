package com.price_comparator.Compr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "website")
public class Website {

    @Id
    @SequenceGenerator(name = "website_sequence", sequenceName =
            "website_sequence", allocationSize = 1)
    @GeneratedValue(strategy = SEQUENCE, generator = "website_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "website_image_url", nullable = false, columnDefinition =
            "TEXT")
    private String websiteImageUrl;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    private String url;

    // @OneToMany(cascade = CascadeType.ALL)
// private Set<Product> product;
    public Website() {
    }

    public Website(String title, String websiteImageUrl, String url) {
        this.title = title;
        this.websiteImageUrl = websiteImageUrl;
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format(
                "Website[title=%d, website_image_url='%s', url='%s']",
                title, websiteImageUrl, url);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getWebsiteImageUrl() {
        return websiteImageUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWebsiteImageUrl(String websiteImageUrl) {
        this.websiteImageUrl = websiteImageUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}