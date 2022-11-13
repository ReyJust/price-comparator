package com.example.demo.Scrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

import com.example.demo.Product.Product;
import com.example.demo.Product.ProductRepository;
import com.example.demo.Website.Website;

public class Argos extends Thread {
  private Website website;

  private String searchPageURL;
  private String userAgent;
  private int pageQty;

  public Argos(Website website, String userAgent) {
    this.website = website;
    this.searchPageURL = website.getUrl() + "/search/monitor/opt/page:%d";
    this.pageQty = 2;
    this.userAgent = userAgent;
  }

  /**
   * Build a search page url using the page_id and get its html.
   * 
   * @param pageNo
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
    System.out.println("[INFO] Fetched Page " + pageNo);
    // System.out.println(searchPage);
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
   * @param searchPage
   * @return
   */
  public List<String> getProductLinks(int pageNo, Document searchPage) {

    List<String> productLinks = new ArrayList<String>();

    try {
      // Select the segmented product list.
      Elements productGroupList = searchPage
          .select("div[data-test=product-list]").first().children();

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

    System.out.println("[" + website.getTitle() + "][PAGE" + pageNo + "] Gathered " + productLinks.size() + " links.");

    return productLinks;
  }

  /**
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
   * 
   * @return Monitor title.
   */
  public String getProductTitle(Document productPage) {
    return productPage.select("span[data-test=product-title]").text();
  }

  /**
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
   * 
   * @return Monitor brand.
   */
  public String getProductBrand(String brandModel) {
    return brandModel.substring(0, brandModel.indexOf(' '));
  }

  /**
   * 
   * @return Monitor model.
   */
  public String getProductModel(String brandModel) {
    return brandModel.substring(brandModel.indexOf(' '), brandModel.length());
  }

  /**
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
   * 
   * @return Monitor size in inches.
   */
  public Integer getProductScreenSize(Element productPage) {
    Integer size = null;
    try {
      String size_w_metric = productPage.select("div.product-description-content-text > ul").first().firstElementChild()
          .text();

      size = Integer.parseInt(size_w_metric.split("in")[0]);
    } catch (Exception e) {
      // Not found. Keep it null.
    }
    return size;
  }

  /**
   * 
   * @return Monitor display resolution in pixels.
   */
  public String getProductDisplayResolution(Element productPage) {
    String res = null;

    try {
      String resRow = productPage.select("div.product-description-content-text > ul > li:contains(Resolution)").text();

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
   * 
   * @return Monitor refresh rate in Hz.
   */
  public Integer getProductRefreshRate(Element productPage) {
    Integer rate = null;
    try {
      String rateRow = productPage.select("div.product-description-content-text > ul > li:contains(refresh rate)")
          .text();
      System.out.println(rateRow);

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

  @Override
  public void run() {
    System.out.println("[INFO] " + website.getTitle() + " Scrapper Started.\n[INFO] Fetching " + pageQty + " pages.");

    for (int pageNo = 1; pageNo <= pageQty; pageNo++) {

      Document searchPage = getSearchPage(pageNo);
      List<String> productLinks = getProductLinks(pageNo, searchPage);

      for (String link : productLinks) {
        // Product Url are relative, adding base.
        Document productPage = getPage(website.getUrl() + link);

        String image = getProductImage(productPage);
        String title = getProductTitle(productPage);

        String brandModel = decomposeTitle(title);

        String brand = getProductBrand(brandModel);
        String model = getProductModel(brandModel);
        Double price = getProductPrice(productPage);
        Integer screenSize = getProductScreenSize(productPage);
        String displayResolution = getProductDisplayResolution(productPage);
        Integer refreshRate = getProductRefreshRate(productPage);

        // System.out.println(link);
        System.out.println(String.format("""
            ----------------\r
            Image: %s\r
            Title: %s\r
            Brand: %s\r
            Model: %s\r
            Price: $ %f\r
            Display size: %d\"\r
            Resolution: %s\r
            Refresh Rate: %d Hz\r
            ----------------\n
            """, image, title, brand, model, price, screenSize, displayResolution,
            refreshRate));

        // Product product = new Product(model, true, title, link, brand,
        // this.website, 0,
        // "test", image, "test", price);

        // productRepository.save(product);

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }

  }
}