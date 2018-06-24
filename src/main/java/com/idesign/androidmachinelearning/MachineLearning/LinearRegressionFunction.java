package com.idesign.androidmachinelearning.MachineLearning;

import com.idesign.androidmachinelearning.BuildConfig;

import java.util.Arrays;
import java.util.function.Function;

public class LinearRegressionFunction implements Function<Double[], Double> {
  private final double[] thetaVector;

  public LinearRegressionFunction(double[] thetaVector) {
    this.thetaVector = Arrays.copyOf(thetaVector, thetaVector.length);
  }

  public Double apply(Double[] featureVector) {
    if (BuildConfig.DEBUG && featureVector[0] != 1.0) {
      throw new AssertionError("feature vector [0] needs to be 1.0");
    }

    double prediction = 0;
    for (int j = 0; j < thetaVector.length; j++) {
      prediction += thetaVector[j] * featureVector[j];
    }
    return prediction;
  }

  public double[] getThetas() {
    return Arrays.copyOf(thetaVector, thetaVector.length);
  }
}
