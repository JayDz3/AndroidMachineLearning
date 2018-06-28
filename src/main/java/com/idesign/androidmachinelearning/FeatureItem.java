package com.idesign.androidmachinelearning;

 public class FeatureItem {
  private double itemTheta, itemFeatureOne, itemFeatureTwo, itemPredictedValue;

  FeatureItem(double itemTheta, double itemFeatureOne, double itemFeatureTwo, double itemPredictedValue) {
    this.itemTheta = itemTheta;
    this.itemFeatureOne = itemFeatureOne;
    this.itemFeatureTwo = itemFeatureTwo;
    this.itemPredictedValue = itemPredictedValue;
  }

  public void setFeatureOne(double itemFeatureOne) {
    this.itemFeatureOne = itemFeatureOne;
  }

  public void setFeatureTwo(double itemFeatureTwo) {
    this.itemFeatureTwo = itemFeatureTwo;
  }

  public void setPredictedValue(double itemPredictedValue) {
    this.itemPredictedValue = itemPredictedValue;
  }

  public double getItemTheta() {
    return itemTheta;
  }

  public double getItemFeatureOne() {
    return itemFeatureOne;
  }

  public double getItemFeatureTwo() {
    return itemFeatureTwo;
  }

  public double getItemPredictedValue() {
    return itemPredictedValue;
  }
}
