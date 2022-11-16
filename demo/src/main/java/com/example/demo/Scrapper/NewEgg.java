package com.example.demo.Scrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;

import com.example.demo.Product.Product;
import com.example.demo.Product.ProductRepository;
import com.example.demo.Website.Website;

public class NewEgg extends Thread {
  private Website website;

  private String searchPageURL;
  private String userAgent;
  private int pageQty;

  public NewEgg(Website website, String userAgent) {
    this.website = website;
    this.searchPageURL = website.getUrl() + "/p/pl?d=monitor&page=%d";
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
      image = productPage.getElementsByClass("product-view-img-original").first().attr("src");
    } catch (Exception e) {
      // TODO: handle exception
    }
    return image;
  }

  /**
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
   * 
   * @return Monitor size in inches.
   */
  public Integer getProductScreenSize(Element productDisplayTable) {
    Integer size = null;

    try {
      String size_w_metric = productDisplayTable.select("tr > th:contains(Screen Size) + td").text();

      size = Integer.parseInt(size_w_metric.split("\"")[0]);
    } catch (Exception e) {
      // Not found. Keep it null.
    }
    return size;
  }

  /**
   * 
   * @return Monitor display resolution in pixels.
   */
  public String getProductDisplayResolution(Element productDisplayTable) {
    String res = null;

    try {
      res = productDisplayTable.select("tr > th:contains(Recommended Resolution) + td").text();

      // Removes the (2K) (4K) at the end
      res = res.substring(0, res.lastIndexOf(" "));

    } catch (Exception e) {
      // Not found. Keep it null.
    }
    return res;
  }

  /**
   * 
   * @return Monitor refresh rate in Hz.
   */
  public Integer getProductRefreshRate(Element productDisplayTable) {
    Integer rate = null;
    try {

      String rate_w_metric = productDisplayTable.select("tr > th:contains(Refresh Rate) + td").text();

      rate = Integer.parseInt(rate_w_metric.split(" ")[0]);
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
    System.out.println("[INFO] " + website.getTitle() + " Scrapper Started.\n[INFO] Fetching " + pageQty + " pages.");
    int total_products = 0;

    for (int pageNo = 1; pageNo <= pageQty; pageNo++) {

      Document searchPage = getSearchPage(pageNo);
      List<String> productLinks = getProductLinks(pageNo, searchPage);

      for (String link : productLinks) {
        Document productPage = getPage(link);

        Element productDetails = productPage.getElementById("product-details");

        String brand = null;
        String model = null;
        Integer screenSize = null;
        String displayResolution = null;
        Integer refreshRate = null;
        try {
          Element productModelTable = productDetails.lastElementChild().child(1).select("caption:contains(Model)")
              .first().parent();
          Element productDisplayTable = productDetails.lastElementChild().child(1).select("caption:contains(Display)")
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

        // System.out.println(link);
        System.out.println(String.format("""
            [%s]------\r
            Link: %s\r
            Image: %s\r
            Title: %s\r
            Brand: %s\r
            Model: %s\r
            Price: $ %f\r
            Display size: %d\"\r
            Resolution: %s\r
            Refresh Rate: %d Hz\r
            ----------------\n
            """, website.getTitle(), link, image, title, brand, model, price, screenSize, displayResolution,
            refreshRate));

        // We keep product which have brand, model and price
        if (brand != null && model != null && price != null) {
          // Product product = new Product(model, true, title, this.website.getUrl() +
          // link, brand, this.website, 0,
          // "test", image, "test", price);

          // productRepository.save(product);
          total_products += 1;

        }

        requestSleep();
      }
      ;
      requestSleep();
    }
    System.out.println("[" + website.getTitle() + "] FINISHED SCRAPPING: Save " + total_products + " valid products.");

  }
}