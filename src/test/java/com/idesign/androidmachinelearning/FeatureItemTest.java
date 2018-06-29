package com.idesign.androidmachinelearning;

import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class FeatureItemTest {
  private FeatureItem featureItem = new FeatureItem(0.0, 0.0, 0.0, 0.0);
  private double baseValue = 0.0;
  private double doubleOne = 1.0;
  private double doubleTwo = 2.0;
  private double doubleThree = 3.0;
  private double doubleFour = 4.0;

  /*=============*
   * Class tests *
   *=============*/
  @Test
  public void featureItem_notNull() {
    assertThat(featureItem, notNullValue());
  }

  @Test
  public void featureItem_isObject() {
    assertThat(featureItem, is(instanceOf(Object.class)));
  }

  @Test
  public void featureItem_isInstance_ofFeatureItem() {
    assertThat(featureItem, is(instanceOf(FeatureItem.class)));
  }

  /*=============*
   * Value Tests *
   *=============*/

  @Test
  public void featureItem_baseItemTheta_baseValue() {
    assertThat(featureItem.getItemTheta(), is(baseValue));
  }

  @Test
  public void featureItem_featureOne_baseValue() {
    assertThat(featureItem.getItemFeatureOne(), is(baseValue));
  }

  @Test
  public void featureItem_featureTwo_baseValue() {
    assertThat(featureItem.getItemFeatureTwo(), is(baseValue));
  }

  @Test
  public void featureItem_predictedValue_baseValue() {
    assertThat(featureItem.getItemPredictedValue(), is(baseValue));
  }

  /*==========================*
   *  Set FeatureItem values  *
   *==========================*/

  @Test
  public void featureItem_setFeatureOne() {
    double featureOneValue = 1.0;
    featureItem.setFeatureOne(featureOneValue);
    assertThat(featureItem.getItemFeatureOne(), is(doubleOne));
  }

  @Test
  public void featureItem_setFeatureTwo() {
    double featureTwoValue = 1.0;
    featureItem.setFeatureTwo(featureTwoValue);
    assertThat(featureItem.getItemFeatureTwo(), is(doubleOne));
  }

  @Test
  public void featureItem_setPredictedValue() {
    double predictedValue = 1.0;
    featureItem.setPredictedValue(predictedValue);
    assertThat(featureItem.getItemPredictedValue(), is(doubleOne));
  }

  /*======================================================================*
   *  Test that setting values to int automatically translates to double  *
   *======================================================================*/
  @Test
  public void featureItem_values_areDoubles() {
    featureItem = new FeatureItem(1, 1, 1, 1);
    assertThat(featureItem.getItemTheta(), is(doubleOne));
    assertThat(featureItem.getItemFeatureOne(), is(doubleOne));
    assertThat(featureItem.getItemFeatureTwo(), is(doubleOne));
    assertThat(featureItem.getItemPredictedValue(), is(doubleOne));
  }

  /*=======================================================*
   *    retrieving all values returns double[] in order    *
   *=======================================================*/
  @Test
  public void featureItem_getValues_areInOrder() {
    featureItem = new FeatureItem(1, 2, 3, 4);
    double[] values = featureItem.getAllvalues();
    assertThat(values[0], is(doubleOne));
    assertThat(values[1], is(doubleTwo));
    assertThat(values[2], is(doubleThree));
    assertThat(values[3], is(doubleFour));
  }

  /*=======================================================*
   *    retrieving item values returns double[] in order    *
   *=======================================================*/
  @Test
  public void featureItem_getItemValues_areInOrder() {
    featureItem = new FeatureItem(1, 2, 3, 4);
    double[] itemValues = featureItem.getItemValues();
    assertThat(itemValues[0], is(doubleTwo));
    assertThat(itemValues[1], is(doubleThree));
    assertThat(itemValues[2], is(doubleFour));
  }
}
