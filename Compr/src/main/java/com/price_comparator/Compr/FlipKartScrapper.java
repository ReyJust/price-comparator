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
 * The Flipkart Scrapper Class
 *
 */
@Component
public class FlipKartScrapper extends Thread {
    private Website website;

    private String searchPageURL;
    private String userAgent;
    private int pageQty;
    @Autowired
    Hibernate hibernate;

    public FlipKartScrapper() {
        this.website = new Website("Flipkart",
                "https://1000logos.net/wp-content/uploads/2021/02/Flipkart-logo.png",
                "https://www.flipkart.com");
        this.searchPageURL = website.getUrl()
                +
                "/search?q=monitor&otracker=search&otracker1=search&marketplace=FLIPKART&as-show=on&as=off&as-pos=1&as-type=HISTORY&as-backfill=on&page=%d";
        this.pageQty = 10;
        this.userAgent = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36";
        ;
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
     * @param searchPage searching page
     * @param pageNo     page number
     * 
     * @return links
     */
    public List<String> getProductLinks(int pageNo, Document searchPage) {

        List<String> productLinks = new ArrayList<String>();

        try {
            Elements pListRow = searchPage.select("div._1YokD2._3Mn1Gg").last().children()
                    .select("div._1AtVbE.col-12-12")
                    .select("div._13oc-S");

            for (Element list : pListRow) {
                Elements ps = list.children();
                for (Element p : ps) {
                    String link = p.firstElementChild().firstElementChild().attr("href");

                    if (link != "") {
                        productLinks.add(link);
                    }
                }
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
            image = productPage.select("div[class=CXW8mj _3nMexc]").select("img[class=_396cs4 _2amPTt _3qGmMb _3exPp9]")
                    .attr("src");

        } catch (Exception e) {
            // Not found. Keep it null.
        }

        return image;
    }

    /**
     * @param productPage product page
     *
     * @return Monitor title.
     */
    public String getProductTitle(Document productPage) {
        return productPage.select("span.B_NuCI").first().text();
    }

    /**
     * @param title product title
     *
     * @return Cannot find Model and Brand separately, Using regex to filter the
     *         product title.
     */
    public String decomposeTitle(String title) {
        String brandModel = null;
        try {
            Pattern regexPattern = Pattern.compile(".+?(?=\\s\\d{2,3})");
            // Identified a pattern in product title.
            // Always: [BRAND] [MODEL ?MODEL] [SIZE] ...
            // Breaks the line when encountering the screen size, 2 or 3 digit, then first
            // word is brand.
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
     * @param title product title
     *
     * @return Monitor brand.
     */
    public String getProductBrand(String title) {
        // First word of title
        return title.substring(0, title.indexOf(' '));
    }

    /**
     * @param productPage product page
     *
     * @return Specification Table.
     */
    public Elements getSpecificationTable(Element productPage) {
        Elements table = null;

        try {
            table = productPage.select("div._3dtsli > div > div:contains(General)").parents();

        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return table;
    }

    /**
     * @param productPage product page
     *
     * @return Display Features Specification Table.
     */
    public Elements getDisplaySpecificationTable(Element productPage) {
        Elements table = null;

        try {
            table = productPage.select("div._3dtsli > div > div:contains(Display Features)").parents();

        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return table;
    }

    /**
     * @param specTable product specification table
     * 
     * @return Monitor model.
     */
    public String getProductModel(Elements specTable) {
        String model = null;

        try {
            model = specTable.select("table > tbody > tr > td:contains(Model Name) + td > ul > li").text();

        } catch (Exception e) {
            // Not found. Keep it null.
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
            String priceRaw = productPage.select("div._30jeq3._16Jk6d").first().text();

            price = Double.parseDouble(priceRaw.substring(1,
                    priceRaw.length()).replace(",", "."));

        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return price;
    }

    /**
     * @param specTable product specification table
     * 
     * @return Monitor size in inches.
     */
    public Double getProductScreenSize(Elements specTable) {
        Double size = null;
        try {
            String displayStr = specTable.select("table > tbody > tr > td:contains(Display) + td > ul > li").text();

            Pattern regexPattern = Pattern.compile("\\((\\d{2}).+\\)");
            Matcher match = regexPattern.matcher(displayStr);

            if (match.find()) {
                size = Double.parseDouble(match.group(1).split(" ")[0]);
            }

        } catch (Exception e) {
            // Not found. Keep it null.
        }
        return size;
    }

    /**
     * @param specTable product specification table
     * 
     * @return Monitor display resolution in pixels.
     */
    public String getProductDisplayResolution(Elements specTable) {
        String res = "";

        try {
            res = specTable.select("table > tbody > tr > td:contains(Resolution) + td > ul > li").first().text();

            res = res.substring(0, res.lastIndexOf(" "));

        } catch (Exception e) {
            // Not found. Keep it null.
        }

        if (res.indexOf('x') == -1) {
            res = null;
        }

        return res;
    }

    /**
     * @param displaySpecTable product display part of specification table
     *
     * @return Monitor refresh rate in Hz.
     */
    public Integer getProductRefreshRate(Elements displaySpecTable) {
        Integer rate = null;

        try {
            String rateWithMetric = displaySpecTable
                    .select("table > tbody > tr > td:contains(Maximum Refresh Rate) + td > ul > li").text();
            rate = Integer.parseInt(rateWithMetric.substring(0, rateWithMetric.indexOf(' ')));

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
        System.out
                .println("[INFO] " + website.getTitle() + " Scrapper Started.\n[INFO] Fetching " + pageQty + " pages.");

        saveWebsite();

        int total_products = 0;

        for (int pageNo = 1; pageNo <= pageQty; pageNo++) {

            Document searchPage;
            searchPage = getSearchPage(pageNo);

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
                String brand = getProductBrand(title);

                Elements specTable = getSpecificationTable(productPage);
                String model = getProductModel(specTable);

                Double price = getProductPrice(productPage);

                Double screenSize = getProductScreenSize(specTable);
                String displayResolution = getProductDisplayResolution(specTable);

                Elements displaySpecTable = getSpecificationTable(productPage);
                Integer refreshRate = getProductRefreshRate(displaySpecTable);

                String productId = model + website.getId();

                // System.out.println(website.getUrl() + link);
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

                // We keep product which have brand, model and price
                if (brand != null && model != null && model != "" && model != " " && price != null) {

                    model = model.trim();

                    Product product = new Product(productId, model, title, website.getUrl() + link, brand, website,
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
            requestSleep();
        }

        System.out.println("[" + website.getTitle() + "] FINISHED SCRAPPING: Save " +
                total_products + " valid products.");

    }
}