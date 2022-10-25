package com.example.demo.Scrapper;

public class Ebay extends Thread {
  private String name;

  public Ebay() {
    this.name = "Ebay Scrapper";
  }

  @Override
  public void run() {
    System.out.println(name + " Started.");
    for (int i = 1; i <= 2; i++) {
      System.out.println(i + " from " + name);

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
    }
  }
}