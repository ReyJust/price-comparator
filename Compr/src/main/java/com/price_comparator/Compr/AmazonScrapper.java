package com.price_comparator.Compr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Amazon Scrapper Class
 *
 */
@Component
public class AmazonScrapper extends Thread {
    private Website website;

    private String searchPageURL;
    private String userAgent;
    private int pageQty;

    @Autowired
    Hibernate hibernate;

    /**
     * The scrapper constructor
     *
     * @param website
     * @param userAgent
     */
    public AmazonScrapper() {
        this.website = new Website("Amazon",
                "https://upload.wikimedia.org/wikipedia/commons/a/a9/Amazon_logo.svg",
                "https://www.amazon.com");
        this.searchPageURL = website.getUrl() +
                "/s?k=monitor&page=%d&ref=nb_sb_noss";
        this.pageQty = 20;
        this.userAgent = "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36";
    }

    /**
     * Add the website to the database
     */
    public void saveWebsite() {
        hibernate.init();
        hibernate.addWebsite(website);

    }

    /**
     * Build a search page url using the page_id and get its html.
     *
     * @param pageNo
     * @return Search Page
     */
    public Document getSearchPage(int pageNo) {
        String url;
        if (pageNo == 1) {
            // Page one have no page param
            url = this.website.getUrl() + "/s?k=monitor&ref=nb_sb_noss";
        } else {
            url = String.format(searchPageURL, pageNo);
        }

        Document searchPage = getPage(url);
        System.out.println("[INFO] Fetched Page " + pageNo + ": " + url);

        return searchPage;
    }

    /**
     * Using the page Url, get its content.
     *
     * @param pageURL
     * @return page
     */
    public Document getPage(String pageURL) {

        Document page = new Document(pageURL);

        try {
            // Connect to the page.
            page = Jsoup.connect(pageURL)
                    .userAgent(this.userAgent).get();
        } catch (Exception e) {
            System.out.println("[ERROR] Failed reaching page " + pageURL);
            e.printStackTrace();
        }

        return page;
    }

    /**
     * From the search page, return each product link found in the search result
     * div.
     *
     * @param pageNo
     * @param searchPage
     * @return productLinks
     */
    public List<String> getProductLinks(int pageNo, Document searchPage) {
        // Select the product list.
        Elements productList = searchPage.select("span[data-component-type=s-search-results]")
                .select("div[data-component-type=s-search-result]");

        List<String> productLinks = new ArrayList<String>();

        for (Element element : productList) {
            // We skip sponsored products and ones without prices
            Element productHavePrice = element.select("span[class=a-price-whole]").first();

            Element isSponsored = element.select("div[class=a-row a-spacing-micro]").first();

            if (productHavePrice != null && isSponsored == null) {
                String productUrl = element
                        .select("a[class=a-link-normal s-underline-text s-underline-link-text s-link-style a-text-normal]")
                        // class="a-link-normal.s-underline-text.s-underline-link-text.s-link-style.a-text-normal"
                        .attr("href");

                productLinks.add(productUrl);
            }

        }
        System.out.println(
                "[" + website.getTitle() + "][PAGE" + pageNo + "] Gathered " + productLinks.size() + " links.");

        return productLinks;
    }

    /**
     * Get the product image
     *
     * @param productPage
     *
     * @return Monitor image.
     */
    public String getProductImage(Document productPage) {
        return productPage.getElementById("landingImage").attr("src");
    }

    /**
     * Get the product title
     *
     * @param productPage
     *
     * @return Monitor title.
     */
    public String getProductTitle(Document productPage) {
        return productPage.getElementById("productTitle").text();
    }

    /**
     * Get the product brand
     *
     * @param productPage
     *
     * @return Monitor brand.
     */
    public String getProductBrand(Document productPage) {
        return productPage.getElementsByClass("po-brand").first().lastElementChild().firstElementChild().text();
    }

    /**
     * Get the product model
     *
     * @param productDetails
     *
     * @return Monitor model.
     */
    public String getProductModel(Element productDetails) {
        String model = "";

        try {
            Element detail_section = productDetails.getElementById("prodDetails");
            Element detail_table = detail_section.getElementById("productDetails_techSpec_section_2");

            if (detail_table == null) {
                detail_table = detail_section.getElementById("productDetails_detailBullets_sections1");
            }

            model = detail_table.select("th:contains(Item model number) + td").text();
        } catch (Exception e) {
            // Not found. Keep it null.
        }

        return model;
    }

    /**
     * Get the product price
     *
     * @param productPage
     *
     * @return Monitor price.
     */
    public Double getProductPrice(Document productPage) {
        Double price = null;

        try {
            Element price_section = productPage.select("div[class=a-box-group]").first()
                    .getElementById("corePrice_feature_div");

            String price_str = price_section.select("span[class=a-offscreen]").first().text();
            price = Double.parseDouble(price_str.substring(1, price_str.length()));
        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return price;
    }

    /**
     *
     * Get the product screen size
     *
     * @param productPage
     *
     * @return Monitor size in inches.
     */
    public Double getProductScreenSize(Document productPage) {
        Double size = null;
        try {
            String size_w_metric = productPage.getElementsByClass("po-display.size").first().lastElementChild()
                    .firstElementChild().text();

            size = Double.parseDouble(size_w_metric.split(" ")[0]);
        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return size;
    }

    /**
     *
     * Get the product display resolution
     *
     * @param productPage
     *
     * @return Monitor display resolution in pixels.
     */
    public String getProductDisplayResolution(Document productPage) {
        String res = null;
        try {

            res = productPage.getElementsByClass("po-display.resolution_maximum").first().lastElementChild()
                    .firstElementChild().text();
        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return res;
    }

    /**
     *
     * Get the product refresh rate
     *
     * @param productPage
     *
     * @return Monitor refresh rate in Hz.
     */
    public Integer getProductRefreshRate(Document productPage) {
        Integer rate = null;
        try {
            String rate_w_metric = productPage.getElementsByClass("po-refresh_rate").first().lastElementChild()
                    .firstElementChild().text();

            rate = Integer.parseInt(rate_w_metric.split(" ")[0]);
        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return rate;
    }

    /*
     * Random Sleep between 1 and 3 seconds.
     *
     * @return null
     */
    public Integer requestSleep() {
        Random rn = new Random();
        int range = 3000 - 2000 + 1;
        int randomNum = rn.nextInt(range) + 2000;
        // System.out.println(randomNum);

        try {
            Thread.sleep(randomNum);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void run() {
        System.out
                .println("[INFO] " + this.website.getTitle() + " Scrapper Started.\n[INFO] Fetching" + pageQty
                        + " pages.");
        int total_products = 0;

        saveWebsite();

        for (int pageNo = 1; pageNo <= pageQty; pageNo++) {

            Document searchPage = getSearchPage(pageNo);
            List<String> productLinks = getProductLinks(pageNo, searchPage);

            if (pageNo == 1 && productLinks.size() == 0) {
                // Bug first page never works, so we retry to get its links
                System.out.println("[DEBUG] Retrying 1st page.");
                searchPage = getSearchPage(pageNo);
                productLinks = getProductLinks(pageNo, searchPage);
            }

            for (String link : productLinks) {
                Document productPage = getPage(this.website.getUrl() + link);
                Element productDetails = productPage.getElementById("productDetails_feature_div");

                String image = getProductImage(productPage);
                String title = getProductTitle(productPage);
                String brand = getProductBrand(productPage);
                String model = getProductModel(productDetails);
                Double price = getProductPrice(productPage);
                Double screenSize = getProductScreenSize(productPage);
                String displayResolution = getProductDisplayResolution(productPage);
                Integer refreshRate = getProductRefreshRate(productPage);

                String productId = model + website.getId();

                // System.out.println(this.website.getUrl() + link);
                System.out.println(String.format("""
                        [%s]------\r
                        Link: %s\r
                        Image: %s\r
                        Title: %s\r
                        Brand: %s\r
                        Model: %s\r
                        Price: $ %f\r
                        Display size: %f\"\r
                        Resolution: %s\r
                        Refresh Rate: %d Hz\r
                        ----------------
                        """, website.getTitle(), website.getUrl() + link, image, title, brand, model,
                        price, screenSize,
                        displayResolution,
                        refreshRate));

                // Data cleaning
                // We keep product which have brand, model and price
                if (brand != null && model != null && model != "" && model != " " && price != null) {
                    model = model.trim();

                    Product product = new Product(productId, model, title, website.getUrl() + link, brand, website,
                            "", image, price);

                    ProductDetails details = new ProductDetails(product, website, screenSize, displayResolution,
                            refreshRate);

                    try {
                        // Products with an already existing id (Model+website_id) are simply not
                        // inserted.
                        // Using a try catch, we prevent the code from crashing when the product is
                        // uncessfully inserted.
                        hibernate.addProduct(product);
                        hibernate.addProductDetails(details);

                    } catch (Exception e) {
                        System.out.println("[ERROR] Failed to save product to db: " + e);
                    }

                    total_products += 1;
                }

                requestSleep();
            }
            ;
            requestSleep();
        }
        hibernate.shutDown();
        System.out.println("[" + website.getTitle() + "] FINISHED SCRAPPING: Save " +
                total_products + " valid products.");

    }
}