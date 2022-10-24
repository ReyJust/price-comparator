package com.example.demo;

public class RunnableObject implements Runnable {

  String name;

  public RunnableObject(String name) {
    this.name = name;
  }

  @Override
  public void run() {
    System.out.println("Started thread " + name);
    try {
      Thread.sleep(2000); // wait 2 seconds
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Ended thread " + name);
  }
}
