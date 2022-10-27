package com.example.demo.Scrapper;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.Product.Product;
import com.example.demo.Product.ProductRepository;
import com.example.demo.Website.Website;

public class Amazon extends Thread {
  private String name;
  private String baseURL;
  private String searchPageURL;
  private String userAgent;
  private int pageQty;
  private Website websiteObject;

  @Autowired
  private ProductRepository productRepository;

  public Amazon(int pageQty) {
    this.name = "Amazon";
    this.baseURL = "https://www.amazon.com/";
    this.searchPageURL = baseURL + "s?k=monitor&page=%d&ref=nb_sb_noss";
    this.userAgent = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
    this.pageQty = pageQty;
    this.websiteObject = new Website("Amazon", "https://upload.wikimedia.org/wikipedia/commons/a/a9/Amazon_logo.svg",
        baseURL);
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
      // Page one have no http page param
      url = baseURL + "s?k=monitor&ref=nb_sb_noss";
    } else {
      url = String.format(searchPageURL, pageNo);
    }

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
      // Connect to the page.
      page = Jsoup.connect(pageURL)
          .userAgent(userAgent).get();
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
    // Select the product list.
    Elements productList = searchPage.select("div[data-component-type=s-search-result]");

    List<String> productLinks = new ArrayList<String>();

    for (Element element : productList) {
      // String asin = element.attr("data-asin");

      // We skip sponsored products and ones without prices
      Element productHavePrice = element.select("span[class=a-price-whole]").first();
      Element isSponsored = element.select("div[class=a-row a-spacing-micro]").first();
      if (productHavePrice != null && isSponsored == null) {
        String productUrl = element
            .select("a[class=a-link-normal s-underline-text s-underline-link-text s-link-style a-text-normal]")
            .attr("href");

        productLinks.add(productUrl);
      }

    }
    System.out.println("[AMAZON][PAGE" + pageNo + "] Gathered " + productLinks.size() + " links.");

    return productLinks;
  }

  /**
   * 
   * @return Monitor image.
   */
  public String getProductImage(Document productPage) {
    return productPage.getElementById("landingImage").attr("src");
  }

  /**
   * 
   * @return Monitor title.
   */
  public String getProductTitle(Document productPage) {
    return productPage.getElementById("productTitle").text();
  }

  /**
   * 
   * @return Monitor brand.
   */
  public String getProductBrand(Document productPage) {
    return productPage.getElementsByClass("po-brand").first().lastElementChild().firstElementChild().text();
  }

  /**
   * 
   * @return Monitor model.
   */
  public String getProductModel(Element productDetails) {

    Element detail_section = productDetails.getElementById("prodDetails");
    Element detail_table = detail_section.getElementById("productDetails_detailBullets_sections1");

    String model = "";

    if (detail_table != null) {
      Elements details = detail_table.firstElementChild().children();

      for (Element detail : details) {
        if (detail.select("th").text().contains("Item model number")) {
          model = detail.select("td").text();
          break;
        }
      }
    }

    return model;
  }

  /**
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
   * @return Monitor size in inches.
   */
  public Integer getProductScreenSize(Document productPage) {
    Integer size = null;
    try {
      String size_w_metric = productPage.getElementsByClass("po-display.size").first().lastElementChild()
          .firstElementChild().text();

      size = Integer.parseInt(size_w_metric.split(" ")[0]);
    } catch (Exception e) {
      // Not found. Keep it null.
    }
    return size;
  }

  /**
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

  @Override
  public void run() {
    System.out.println("[INFO] " + name + " Scrapper Started.\n[INFO] Fetching " + pageQty + " pages.");

    for (int pageNo = 1; pageNo <= pageQty; pageNo++) {

      Document searchPage = getSearchPage(pageNo);
      List<String> productLinks = getProductLinks(pageNo, searchPage);

      for (String link : productLinks) {
        Document productPage = getPage(baseURL + link);
        Element productDetails = productPage.getElementById("productDetails_feature_div");

        System.out.println(baseURL + link);

        String image = getProductImage(productPage);
        String title = getProductTitle(productPage);
        String brand = getProductBrand(productPage);
        String model = getProductModel(productDetails);
        Double price = getProductPrice(productPage);
        Integer screenSize = getProductScreenSize(productPage);
        String displayResolution = getProductDisplayResolution(productPage);
        Integer refreshRate = getProductRefreshRate(productPage);

        // System.out.println("Image: " + image);
        // System.out.println("Title: " + title);
        // System.out.println("Brand: " + brand);
        // System.out.println("Model: " + model);
        // System.out.println("Price: " + price);
        // System.out.println("Display size: " + screenSize);
        // System.out.println("Resolution: " + displayResolution);
        // System.out.println("Refresh Rate: " + refreshRate);

        Product product = new Product(model, true, title, baseURL + link, brand, this.websiteObject, 0,
            "test", image, "test", price);

        productRepository.save(product);

      }
      ;
      // Elements product_list =
      // search_page.select("div[data-component-type=s-search-result]");

      // getSearchPageProductList

      // for (Element element : product_list) {
      // String asin = element.attr("data-asin");
      // System.out.println("[AMAZON][PAGE" + page_id + "] " + asin);
      // String product_title =
      // element.select("span.a-size-medium.a-color-base.a-text-normal").text();
      // Elements product_prices = element.select("span.a-offscreen");
      // float product_price;

      // if (product_prices.size() > 1) {
      // // Take the first price
      // String price = product_prices.first().text();
      // // Remove the currency sign
      // product_price = Float.parseFloat(price.substring(1, price.length()));
      // } else {
      // product_price = 0;
      // }
      // String[] splitted_title = product_title.split(" ");
      // System.out.println(splitted_title);
      // String brand = splitted_title[0];
      // String model = splitted_title[1];

      // System.out.println(product_title + brand + model);

      // }

    }

  }
}