package com.example.demo.Scrapper;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Amazon extends Thread {
  private String name;

  public Amazon() {
    this.name = "Amazon Scrapper";
  }

  @Override
  public void run() {
    System.out.println(name + " Started.");
    int qty_of_page = 2;

    for (int page_id = 1; page_id <= qty_of_page; page_id++) {
      String url = "https://www.amazon.com/s?k=monitor&page=" + page_id + "&ref=nb_sb_noss";

      try {
        Document search_page = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
            .get();
        Elements product_list = search_page.select("div[data-component-type=s-search-result]");

        for (Element element : product_list) {
          String asin = element.attr("data-asin");
          System.out.println("[AMAZON][PAGE" + page_id + "] " + asin);
          String product_title = element.select("span.a-size-medium.a-color-base.a-text-normal").text();
          Elements product_prices = element.select("span.a-offscreen");
          float product_price;

          if (product_prices.size() > 1) {
            // Take the first price
            String price = product_prices.first().text();
            // Remove the currency sign
            product_price = Float.parseFloat(price.substring(1, price.length()));
          } else {
            product_price = 0;
          }

          System.out.println(product_price);

        }

      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }
}