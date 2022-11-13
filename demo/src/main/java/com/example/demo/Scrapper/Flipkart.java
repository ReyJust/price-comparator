package com.example.demo.Scrapper;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
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

public class Flipkart extends Thread {
  private Website website;

  private String searchPageURL;
  private String userAgent;
  private int pageQty;

  public Flipkart(Website website, String userAgent) {
    this.website = website;
    this.searchPageURL = website.getUrl()
        + "/search?q=monitor&otracker=search&otracker1=search&marketplace=FLIPKART&as-show=on&as=off&as-pos=1&as-type=HISTORY&as-backfill=on&page=%d";
    this.pageQty = 2;
    this.userAgent = userAgent;
  }

  /**
   * Build a search page url using the page_id and get its html.
   * 
   * @param pageNo
   * @return Search Page
   * @throws FileNotFoundException
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
      Elements pListRow = searchPage.select("div._1YokD2._3Mn1Gg").last().children().select("div._1AtVbE.col-12-12")
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
      image = productPage.select("img._396cs4._2amPTt._3qGmMb._3exPp9").attr("href");

    } catch (Exception e) {
      // Not found. Keep it null.
    }

    return image;
  }

  /**
   * 
   * @return Monitor title.
   */
  public String getProductTitle(Document productPage) {
    return productPage.select("span.B_NuCI").first().text();
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
  public String getProductBrand(String title) {
    // First word of title
    return title.substring(0, title.indexOf(' '));
  }

  /**
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
   * 
   * @return Monitor price.
   */
  public Double getProductPrice(Document productPage) {
    Double price = null;

    try {
      String priceRaw = productPage.select("div._30jeq3._16Jk6d").first().text();

      price = Double.parseDouble(priceRaw.substring(1, priceRaw.length()).replace(",", "."));

    } catch (Exception e) {
      // Not found. Keep it null.
    }
    return price;
  }

  /**
   * 
   * @return Monitor size in inches.
   */
  public Integer getProductScreenSize(Elements specTable) {
    Integer size = null;
    try {
      String displayStr = specTable.select("table > tbody > tr > td:contains(Display) + td > ul > li").text();

      Pattern regexPattern = Pattern.compile("\\((\\d{2}).+\\)");
      Matcher match = regexPattern.matcher(displayStr);

      if (match.find()) {
        size = Integer.parseInt(match.group(1).split(" ")[0]);
      }

    } catch (Exception e) {
      // Not found. Keep it null.
    }
    return size;
  }

  /**
   * 
   * @return Monitor display resolution in pixels.
   */
  public String getProductDisplayResolution(Elements specTable) {
    String res = null;

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

  @Override
  public void run() {
    System.out.println("[INFO] " + website.getTitle() + " Scrapper Started.\n[INFO] Fetching " + pageQty + " pages.");

    for (int pageNo = 1; pageNo <= pageQty; pageNo++) {

      Document searchPage;
      searchPage = getSearchPage(pageNo);

      List<String> productLinks = getProductLinks(pageNo, searchPage);

      for (String link : productLinks) {

        // Product Url are relative, adding base.
        Document productPage = getPage(website.getUrl() + link);

        String image = getProductImage(productPage);
        String title = getProductTitle(productPage);
        String brand = getProductBrand(title);

        Elements specTable = getSpecificationTable(productPage);
        String model = getProductModel(specTable);

        Double price = getProductPrice(productPage);

        Integer screenSize = getProductScreenSize(specTable);
        String displayResolution = getProductDisplayResolution(specTable);

        Elements displaySpecTable = getSpecificationTable(productPage);
        Integer refreshRate = getProductRefreshRate(displaySpecTable);

        System.out.println(website.getUrl() + link);
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