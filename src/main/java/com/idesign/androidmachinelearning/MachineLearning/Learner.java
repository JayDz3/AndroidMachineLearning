package com.idesign.androidmachinelearning.MachineLearning;

import java.util.List;

public class Learner {

  public static LinearRegressionFunction train(LinearRegressionFunction targetFunction, List<Double[]> dataset, List<Double> labels, double alpha) {
    int m = dataset.size();
    double[] thetaVector = targetFunction.getThetas();
    double[] newThetaVector = new double[thetaVector.length];

    // compute new theta of each element of the theta array
    for (int j = 0; j < thetaVector.length; j++) {

      double sumErrors = 0;

      for (int i = 0; i < m; i++) {
        Double[] featureVector = dataset.get(i);
        double error = targetFunction.apply(featureVector) - labels.get(i);
        sumErrors += error * featureVector[j];
      }

      // applies weight to theta's
      double gradient = (1.0 / m) * sumErrors;
      newThetaVector[j] = thetaVector[j] - alpha * gradient;
    }
    return new LinearRegressionFunction(newThetaVector);
  }
}