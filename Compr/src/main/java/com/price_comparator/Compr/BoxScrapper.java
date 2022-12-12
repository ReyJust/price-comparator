package com.price_comparator.Compr;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoxScrapper extends Thread {
    private Website website;
    private String searchPageURL;
    private String userAgent;
    private int pageQty;

    @Autowired
    Hibernate hibernate;

    public BoxScrapper() {
        this.website = new Website("The Box", "https://www.box.co.uk/Images/box-logo2-FP_2110111013.svg",
                "https://www.box.co.uk");
        this.searchPageURL = website.getUrl() + "/monitors";
        this.pageQty = 10;
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
     * Using the page Url, get its content.
     *
     * @param pageURL
     * @return page
     */
    public Document getPage(String pageURL) {

        Document page = new Document(pageURL);

        try {
            // Connect to the page.
            page = Jsoup.connect(pageURL).userAgent(userAgent).get();

            return page;

        } catch (Exception e) {
            System.out.println("[ERROR] Failed reaching page " + pageURL);
            e.printStackTrace();
        }

        return page;
    }

    /**
     * Build a search page url using the page_id and get its html.
     *
     * @param pageNo
     * @return Search Page
     */
    public Document getSearchPage(int pageNo) {
        String url;
        if (pageNo != 1) {
            url = searchPageURL + "/page/" + pageNo;
        } else {
            url = searchPageURL;
        }

        Document searchPage = getPage(url);
        System.out.println("[INFO] Fetched Page " + pageNo + ": " + url);
        // System.out.println(searchPage);
        return searchPage;
    }

    /**
     * Retrieve the search result secton from the search page.
     *
     * @param pageNo
     * @return Product List
     */
    public Elements getProductList(int pageNo, Document searchPage) {
        Elements productList = null;

        try {
            // Select the product list.
            productList = searchPage.select("div.product-list").select("div.product-list-item");

            System.out.println("[" + website.getTitle() + "][PAGE" + pageNo + "] Gathered " + productList.size()
                    + " products element.");

        } catch (Exception e) {
            // DO nothing.
        }

        return productList;
    }

    /**
     * From an element of the search list get the product url
     *
     * @param productElement
     * @return
     */
    public String getProductLink(Element productElement) {
        String productLink = null;

        try {
            productLink = productElement.select("div.p-list-title-wrapper").select("a").attr("href");

        } catch (Exception e) {
            // DO nothing.
        }

        return productLink;
    }

    /**
     * @param productElement
     *
     * @return Monitor image.
     */
    public String getProductImage(Element productElement) {
        String image = null;
        try {

            image = productElement.select("div.p-list-image-wrapper").select("img").attr("data-src");

        } catch (Exception e) {
            // Not found. Keep it null.
        }

        return website.getUrl() + image;
    }

    /**
     * @param productElement
     *
     * @return Monitor title.
     */
    public String getProductTitle(Element productElement) {
        String title = null;

        try {
            title = productElement.select("div.p-list-title-wrapper").select("a").text();

        } catch (Exception e) {
            // DO nothing.
        }

        return title;
    }

    /**
     * @return Monitor brand.
     */
    public String getProductBrand(String title) {
        String brand = title.substring(0, title.indexOf(' '));

        return brand;
    }

    /**
     * @return Monitor model.
     */
    public String getProductModel(Element productElement) {
        String model = null;

        try {
            model = productElement.select("div.p-list-title-wrapper").select("p.p-list-manufacturercode").text();

        } catch (Exception e) {
            // DO nothing.
        }

        return model;
    }

    /**
     * @return Monitor price.
     */
    public Double getProductPrice(Element productElement) {
        Double price = null;

        try {
            String price_raw = productElement.select("div.p-list-price-wrapper").select("p.p-list-sell > span.pq-price")
                    .text();
            price = Double.parseDouble(price_raw.substring(1, price_raw.length()));

        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return price;
    }

    /**
     * @param productElement
     *
     * @return Monitor size in inches.
     */
    public Double getProductScreenSize(Element productElement) {
        Double size = null;
        try {
            String size_w_metric = productElement.select("div.p-list-points-wrapper").select("li:contains(Display)")
                    .text();

            size = Double.parseDouble(size_w_metric.split("\"")[0]);

        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return size;
    }

    /**
     * @param productElement
     *
     * @return Monitor display resolution in pixels.
     */
    public String getProductDisplayResolution(Element productElement) {
        String res = null;

        try {
            String resRow = productElement.select("div.p-list-points-wrapper").select("li:contains(Resolution)").text();

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
     * @param productElement
     *
     * @return Monitor refresh rate in Hz.
     */
    public Integer getProductRefreshRate(Element productElement) {
        Integer rate = null;
        try {
            String rateRow = productElement.select("div.p-list-points-wrapper").select("li:contains(Refresh Rate) ")
                    .text();

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

    /*
     * Random Sleep between 1 and 3 seconds.
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

        saveWebsite();

        int total_products = 0;

        for (int pageNo = 1; pageNo <= pageQty; pageNo++) {

            Document searchPage = getSearchPage(pageNo);
            Elements productList = getProductList(pageNo, searchPage);

            if (pageNo == 1 && productList.size() == 0) {
                // Bug first page never works, so we retry to get its links
                System.out.println("[DEBUG] Retrying 1st page.");
                searchPage = getSearchPage(pageNo);
                productList = getProductList(pageNo, searchPage);
            }

            // Since We can gather all the information require of a product directly from
            // the rich search page, we do not need to visit product page individually.
            for (Element productElement : productList) {
                String link = getProductLink(productElement);
                String image = getProductImage(productElement);
                String title = getProductTitle(productElement);
                Double price = getProductPrice(productElement);
                Double screenSize = getProductScreenSize(productElement);
                String displayResolution = getProductDisplayResolution(productElement);
                Integer refreshRate = getProductRefreshRate(productElement);

                String brand = getProductBrand(title);
                String model = getProductModel(productElement);

                String productId = model + website.getId();

                // System.out.println(productLink);
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
                        """, website.getTitle(), link, image, title, brand, model, price, screenSize, displayResolution,
                        refreshRate));

                // We keep product which have brand, model and price
                if (brand != null && model != null && model != "" && model != " " && price != null) {
                    model = model.trim();

                    Product product = new Product(productId, model, title, link, brand, website,
                            "", image, price);

                    ProductDetails details = new ProductDetails(product, website, screenSize, displayResolution,
                            refreshRate);

                    try {
                        hibernate.addProduct(product);
                        hibernate.addProductDetails(details);

                    } catch (Exception e) {
                        System.out.println("[ERROR] Failed to save product to db: " + e);
                    }

                    total_products += 1;

                }

            }
            requestSleep();

        }

        System.out.println(
                "[" + website.getTitle() + "] FINISHED SCRAPPING: Save " + total_products + " valid products.");
    }
}