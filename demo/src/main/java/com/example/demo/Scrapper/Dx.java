package com.example.demo.Scrapper;

public class Dx extends Thread {
  private String name;

  public Dx() {
    this.name = "Dx Scrapper";
    /*
     * https://www.adorama.com/l/Computers/Computer-Monitors-and-Mounts/Computer-
     * Monitors
     */
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