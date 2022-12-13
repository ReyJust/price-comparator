package com.price_comparator.Compr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The New Egg Scrapper Class
 *
 */
@Component
public class NewEggScrapper extends Thread {
    private Website website;
    private String searchPageURL;
    private String userAgent;
    private int pageQty;

    @Autowired
    Hibernate hibernate;

    public NewEggScrapper() {
        this.website = new Website("NewEgg",
                "https://c1.neweggimages.com/WebResource/Themes/Nest/logos/Newegg_full_color_logo_RGB.SVG",
                "https://www.newegg.com");
        this.searchPageURL = website.getUrl() + "/p/pl?d=monitor&page=%d";
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
     * Build a search page url using the page_id and get its html.
     *
     * @param pageNo page number
     * @return Search Page
     */
    public Document getSearchPage(int pageNo) {
        String url;
        // if (pageNo == 1) {
        // Page one have no http page param
        // url = baseURL + "s?k=monitor&ref=nb_sb_noss";
        // } else {
        url = String.format(searchPageURL, pageNo);
        // }

        Document searchPage = getPage(url);
        System.out.println("[INFO] Fetched Page " + pageNo + ": " + url);
        // System.out.println(searchPage);
        return searchPage;
    }

    /**
     * Using the page Url, get its content.
     *
     * @param pageURL page url
     * @return page
     */
    public Document getPage(String pageURL) {

        Document page = new Document(pageURL);

        try {
            // Connect to the page.
            page = Jsoup.connect(pageURL)
                    .userAgent(userAgent).timeout(0).get();
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
     * @param pageNo     page Number to fetch
     * @param searchPage searching page
     * 
     * @return Page product links
     */
    public List<String> getProductLinks(int pageNo, Document searchPage) {
        // Select the product list.
        Elements productList = searchPage
                .select("div.item-cells-wrap.border-cells.items-grid-view.four-cells.expulsion-one-cell");

        List<String> productLinks = new ArrayList<String>();

        try {
            productList = productList.first().children();

            for (Element element : productList) {
                String productUrl = element
                        .select("a[class=item-title]")
                        .attr("href");
                productLinks.add(productUrl);

                continue;

            }
        } catch (Exception e) {
            // DO nothing.
        }

        System.out.println(
                "[" + website.getTitle() + "][PAGE" + pageNo + "] Gathered " + productLinks.size() + " links.");

        return productLinks;
    }

    /**
     * @param productPage product page
     * 
     * @return Monitor image.
     */
    public String getProductImage(Document productPage) {
        String image = null;

        try {
            image = productPage.getElementsByClass("product-view-img-original").first().attr("src");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return image;
    }

    /**
     * @param productPage product page
     * 
     * @return Monitor title.
     */
    public String getProductTitle(Document productPage) {
        String title = null;
        try {
            title = productPage.select("h1.product-title").first().text();

        } catch (Exception e) {
            // TODO: handle exception
        }

        return title;
    }

    /**
     * @param productModelTable model spec table
     * 
     * @return Monitor brand.
     */
    public String getProductBrand(Element productModelTable) {
        String brand = null;
        try {

            brand = productModelTable.select("tr > th:contains(Brand) + td").text();

            if (brand.contains("Universal")) {
                brand = null;
            } else if (brand.contains("\"")) {
                brand = null;
            }

        } catch (Exception e) {
            // Not found. Keep it null.
        }

        return brand;
    }

    /**
     * @param productModelTable model spec table
     * @param brand             Product Brand
     * 
     * @return Monitor model.
     */
    public String getProductModel(Element productModelTable, String brand) {
        String model = null;
        try {
            // model = productModelTable.get(1).text();
            model = productModelTable.select("tr > th:contains(Model) + td").text();

        } catch (Exception e) {
            // Not found. Keep it null.
        }
        if (brand == null) {
            return null;
        }

        return model;
    }

    /**
     * @param productPage product page
     * 
     * @return Monitor price.
     */
    public Double getProductPrice(Document productPage) {
        Double price = null;

        try {
            String price_raw = productPage.select("div[class=product-price]").first().select("li.price-current").text();

            price = Double.parseDouble(price_raw.substring(1, price_raw.length()));

        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return price;
    }

    /**
     * Product Screen size
     * 
     * @param productDisplayTable display part of spec table
     * 
     * @return Monitor size in inches.
     */
    public Double getProductScreenSize(Element productDisplayTable) {
        Double size = null;

        try {
            String size_w_metric = productDisplayTable.select("tr > th:contains(Screen Size) + td ").text();

            size = Double.parseDouble(size_w_metric.split("\"")[0]);
        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return size;
    }

    /**
     * @param productDisplayTable display part of spec table
     * 
     * @return Monitor display resolution in pixels.
     */
    public String getProductDisplayResolution(Element productDisplayTable) {
        String res = null;

        try {
            res = productDisplayTable.select("tr > th:contains(Recommended Resolution) + td").text();

            // Split str to get the 0000 x 0000 pattern.
            Pattern regexPattern = Pattern.compile("(\\b\\d+\\sx\\s\\d+\\b)");
            Matcher match = regexPattern.matcher(res);

            if (match.find()) {
                res = match.group(0);
            }

        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return res;
    }

    /**
     * @param productDisplayTable display part of spec table
     * 
     * @return Monitor refresh rate in Hz.
     */
    public Integer getProductRefreshRate(Element productDisplayTable) {
        Integer rate = null;
        try {

            String rate_w_metric = productDisplayTable.select("tr > th:contains(Refresh Rate) + td ").text();

            rate = Integer.parseInt(rate_w_metric.split(" ")[0]);
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
        System.out.println("[INFO] " + website.getTitle() + " Scrapper Started.\n[INFO]Fetching" + pageQty + " pages.");
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
                Document productPage = getPage(link);

                Element productDetails = productPage.getElementById("product-details");

                String brand = null;
                String model = null;
                Double screenSize = null;
                String displayResolution = null;
                Integer refreshRate = null;
                try {
                    Element productModelTable = productDetails.lastElementChild().child(1)
                            .select("caption:contains(Model)")
                            .first().parent();
                    Element productDisplayTable = productDetails.lastElementChild().child(1)
                            .select("caption:contains(Display)")
                            .first().parent();

                    brand = getProductBrand(productModelTable);
                    model = getProductModel(productModelTable, brand);
                    screenSize = getProductScreenSize(productDisplayTable);
                    displayResolution = getProductDisplayResolution(productDisplayTable);
                    refreshRate = getProductRefreshRate(productDisplayTable);

                } catch (Exception e) {
                    // TODO: handle exception
                }

                String image = getProductImage(productPage);
                String title = getProductTitle(productPage);
                Double price = getProductPrice(productPage);

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
                        ----------------\n
                        """, website.getTitle(), link, image, title, brand, model, price, screenSize,
                        displayResolution,
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

                requestSleep();
            }
            ;
            requestSleep();
        }
        System.out.println("[" + website.getTitle() + "] FINISHED SCRAPPING: Save " +
                total_products + " valid products.");

    }
}