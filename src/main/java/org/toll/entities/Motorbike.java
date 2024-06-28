package org.toll.entities;

public class Motorbike implements Vehicle {
  @Override
  public String getType() {
    return "Motorbike";
  }

  @Override
  public boolean isFeeFree() {
    return false;
  }
}
