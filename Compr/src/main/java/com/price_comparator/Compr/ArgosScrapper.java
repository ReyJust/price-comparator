package com.price_comparator.Compr;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for agos Scrapper
 */
@Component
public class ArgosScrapper extends Thread {
    private Website website;

    private String searchPageURL;
    private String userAgent;
    private int pageQty;

    @Autowired
    Hibernate hibernate;

    /**
     * ArgosScrapper constructor
     *
     */
    public ArgosScrapper() {
        this.website = new Website("Argos", "https://media.4rgos.it/i/Argos/logo_argos2x?w=120&h=103&qlt=75&fmt=png",
                "https://www.argos.co.uk");
        this.searchPageURL = website.getUrl() + "/search/monitor/opt/page:%d";
        this.pageQty = 4;
        this.userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";
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
        url = String.format(searchPageURL, pageNo);

        Document searchPage = getPage(url);
        System.out.println("[INFO] Fetched Page " + pageNo);

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
            page = Jsoup.connect(pageURL).userAgent(userAgent).timeout(0).get();
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
     * @param searchPage
     *
     * @return productLinks
     */
    public List<String> getProductLinks(int pageNo, Document searchPage) {

        List<String> productLinks = new ArrayList<String>();

        try {
            // Select the segmented product list.
            Elements productGroupList = searchPage.select("div[data-test=product-list]").first().children();

            // Loop in each list part
            for (Element group : productGroupList) {
                Elements list = group.firstElementChild().children();
                // Loop in each product of the part
                for (Element product : list) {
                    String productUrl = product.firstElementChild().firstElementChild().firstElementChild().select("a")
                            .attr("href");
                    productLinks.add(productUrl);

                }

            }
        } catch (Exception e) {
            // DO nothing.
        }

        System.out
                .println("[" + website.getTitle() + "][PAGE" + pageNo + "] Gathered " + productLinks.size() + "links.");

        return productLinks;
    }

    /**
     * Get product image
     *
     * @param productPage
     *
     * @return Monitor image.
     */
    public String getProductImage(Document productPage) {
        String image = null;
        try {

            Element imageContainer = productPage.select("div[data-test=component-media-gallery]").first();

            image = imageContainer.firstElementChild().firstElementChild().select("picture > img").attr("src");
        } catch (Exception e) {
            // Not found. Keep it null.
        }
        // HTTP protocol not provided in link.
        return "https" + image;
    }

    /**
     * Get the product title
     *
     * @param productPage
     *
     * @return Monitor title.
     */
    public String getProductTitle(Document productPage) {
        return productPage.select("span[data-test=product-title]").text();
    }

    /**
     * Cannot find Model and Brand separately, Using regex to filter the
     * product title.
     *
     * @param title
     *
     * @return title
     */
    public String decomposeTitle(String title) {
        String brandModel = null;
        try {
            Pattern regexPattern = Pattern.compile(".+?(?=\\s\\d{2,3})");
            // Identified a pattern in product title.
            // Always: [BRAND] [MODEL ?MODEL] [SIZE] ...
            // Breaks the line when encountering the screen size, 2 or 3 digit, then
            // first word is brand.
            Matcher match = regexPattern.matcher(title);

            if (match.find()) {
                brandModel = match.group(0);
            }

        } catch (Exception e) {
            // Not found. Keep it null.
        }

        return brandModel;
    }

    /**
     * Get the product brand
     * 
     * @param brandModel
     *
     * @return Monitor brand.
     */
    public String getProductBrand(String brandModel) {
        String brand = null;
        try {
            brand = brandModel.substring(0, brandModel.indexOf(' '));
        } catch (Exception e) {
            // TODO: handle exception
        }

        return brand;
    }

    /**
     * Get the product brand
     *
     * @param brandModel
     *
     * @return Monitor model.
     */
    public String getProductModel(String brandModel) {
        String model = null;
        try {
            model = brandModel.substring(brandModel.indexOf(' '), brandModel.length());
        } catch (Exception e) {
            // TODO: handle exception
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
            String price_raw = productPage.select("li[data-test=product-price-primary] > h2").text();

            price = Double.parseDouble(price_raw.substring(1, price_raw.length()));

        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return price;
    }

    /**
     * Get the product screen size
     *
     * @param productPage
     *
     * @return Monitor size in inches.
     */
    public Double getProductScreenSize(Element productPage) {
        Double size = null;
        try {
            String size_w_metric = productPage.select("div.product-description-content-text > ul").first()
                    .firstElementChild().text();

            size = Double.parseDouble(size_w_metric.split("in")[0]);
        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return size;
    }

    /**
     * Get the product refresh display resolution
     *
     * @param productPage
     *
     * @return Monitor display resolution in pixels.
     */
    public String getProductDisplayResolution(Element productPage) {
        String res = null;

        try {
            String resRow = productPage.select("div.product-description-content-text > ul > li:contains(Resolution) ")
                    .text();

            // Split str to get the 0000 x 0000 pattern.
            Pattern regexPattern = Pattern.compile("(\\b\\d+\\sx\\s\\d+\\b)");
            Matcher match = regexPattern.matcher(resRow);

            if (match.find()) {
                res = match.group(0);
            }

        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return res;
    }

    /**
     * Get the product refresh rate
     *
     * @param productPage
     *
     * @return Monitor refresh rate in Hz.
     */
    public Integer getProductRefreshRate(Element productPage) {
        Integer rate = null;
        try {
            String rateRow = productPage
                    .select("div.product-description-content-text > ul > li:contains(refresh rate) ").text();

            // Split str to get the 00Hz pattern excluding Hz.
            Pattern regexPattern = Pattern.compile("(\\b\\d{2,3})Hz\\b");
            Matcher match = regexPattern.matcher(rateRow);

            if (match.find()) {
                rate = Integer.parseInt(match.group(1));
            }
        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return rate;
    }

    /**
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
        System.out.println("[INFO] " + website.getTitle() + " Scrapper Started.\n[INFO]Fetching" + pageQty + "pages.");
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
                // Product Url are relative, adding base.
                Document productPage = getPage(website.getUrl() + link);

                String image = getProductImage(productPage);
                String title = getProductTitle(productPage);

                String brandModel = decomposeTitle(title);

                String brand = getProductBrand(brandModel);
                String model = getProductModel(brandModel);
                Double price = getProductPrice(productPage);
                Double screenSize = getProductScreenSize(productPage);
                String displayResolution = getProductDisplayResolution(productPage);
                Integer refreshRate = getProductRefreshRate(productPage);

                String productId = model + website.getId();
                // System.out.println(link);
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
                        """, website.getTitle(), website.getUrl() + link, image, title, brand, model, price, screenSize,
                        displayResolution, refreshRate));

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
            requestSleep();
        }

        System.out.println(
                "[" + website.getTitle() + "] FINISHED SCRAPPING: Save " + total_products + " valid products.");

    }
}
