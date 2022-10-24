package com.example.demo.Scrapper;

public class Amazon extends Thread {
  private String name;

  public Amazon() {
    this.name = "Amazon Scrapper";
  }

  @Override
  public void run() {
    System.out.println(name + " Started.");
    for (int i = 1; i <= 5; i++) {
      System.out.println(i + " from " + name);

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
      }
    }
  }
}