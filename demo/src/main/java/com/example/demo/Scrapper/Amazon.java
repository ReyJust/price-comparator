package com.example.demo.Scrapper;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

import com.example.demo.Product.Product;
import com.example.demo.Product.ProductRepository;
import com.example.demo.Website.Website;

// @Component
public class Amazon extends Thread {
  private Website website;

  private String searchPageURL;
  private String userAgent;
  private int pageQty;

  // @Autowired
  private ProductRepository productRepository;

  public Amazon(Website website, String userAgent) {
    this.website = website;
    this.searchPageURL = website.getUrl() + "/s?k=monitor&page=%d&ref=nb_sb_noss";
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
    if (pageNo == 1) {
      // Page one have no http page param
      url = this.website.getUrl() + "/s?k=monitor&ref=nb_sb_noss";
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
    System.out.println("[" + website + "][PAGE" + pageNo + "] Gathered " + productLinks.size() + " links.");

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
    System.out
        .println("[INFO] " + this.website.getTitle() + " Scrapper Started.\n[INFO] Fetching " + pageQty + " pages.");

    for (int pageNo = 1; pageNo <= pageQty; pageNo++) {

      Document searchPage = getSearchPage(pageNo);
      List<String> productLinks = getProductLinks(pageNo, searchPage);

      for (String link : productLinks) {
        Document productPage = getPage(this.website.getUrl() + link);
        Element productDetails = productPage.getElementById("productDetails_feature_div");

        String image = getProductImage(productPage);
        String title = getProductTitle(productPage);
        String brand = getProductBrand(productPage);
        String model = getProductModel(productDetails);
        Double price = getProductPrice(productPage);
        Integer screenSize = getProductScreenSize(productPage);
        String displayResolution = getProductDisplayResolution(productPage);
        Integer refreshRate = getProductRefreshRate(productPage);

        // System.out.println(this.website.getUrl() + link);
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

        Product product = new Product(model, true, title, this.website.getUrl() + link, brand, this.website, 0,
            "test", image, "test", price);

        // productRepository.save(product);

      }
      ;

    }

  }
}