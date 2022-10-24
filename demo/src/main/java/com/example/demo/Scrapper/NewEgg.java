package com.example.demo.Scrapper;

public class NewEgg extends Thread {
  private String name;

  public NewEgg() {
    this.name = "NewEgg Scrapper";
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