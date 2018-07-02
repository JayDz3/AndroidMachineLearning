package com.idesign.androidmachinelearning;

import com.idesign.androidmachinelearning.Interfaces.OnAddItem;
import com.idesign.androidmachinelearning.Interfaces.OnSetItems;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class FeatureItemAdapterTest implements OnAddItem<FeatureItem>, OnSetItems<FeatureItem> {

  private List<FeatureItem> items = new ArrayList<>();
  private FeatureItemAdapter adapter = new FeatureItemAdapter(items);

  private final double doubleZero = 0.0;
  private final double doubleOne = 1.0;

  private final int sizeZero = 0;
  private final int sizeTwo = 2;

  /*===============*
   *  Class Tests  *
   *===============*/
  @Test
  public void adapter_notNull() {
    assertThat(adapter, notNullValue());
  }

  @Test
  public void adapter_instanceOf_FeatureItemAdapter() {
    assertThat(adapter, is(instanceOf(FeatureItemAdapter.class)));
  }

  @Test
  public void adapter_instanceOf_object() {
    assertThat(adapter, is(instanceOf(Object.class)));
  }

  /*=========================*
   *   Adapter Items Tests   *
   *=========================*/
  @Test
  public void adapter_getItems_notNull() {
    assertThat(adapter.getItems(), notNullValue());
  }

  @Test
  public void adapter_getItems_sizeIsZero() {
    assertThat(adapter.getItems().size(), is(sizeZero));
  }

  @Test
  public void adapter_setItems_getItemCount() {
    FeatureItem featureItem = new FeatureItem(1.0, 0.0, 0.0, 0.0);
    FeatureItem featureItem2 = new FeatureItem(1, 1, 1, 1);
    addItem(items, featureItem);
    addItem(items, featureItem2);
    adapter.setItemsTest(items);
    assertThat(adapter.getItemCount(), is(sizeTwo));
  }

  @Test
  public void adapter_setItems_getItemThetas() {
    FeatureItem featureItem = new FeatureItem(1.0, 0.0, 0.0, 0.0);
    FeatureItem featureItem2 = new FeatureItem(1, 1, 1, 1);
    List<FeatureItem> featureItems = new ArrayList<>();
    featureItems.add(featureItem);
    featureItems.add(featureItem2);

    setList(featureItems);
    adapter.setItemsTest(items);

    FeatureItem item1 = adapter.getItems().get(0);
    FeatureItem item2 = adapter.getItems().get(1);
    assertThat(item1.getItemTheta(), is(doubleOne));
    assertThat(item2.getItemTheta(), is(doubleOne));
  }

  @Test
  public void adapter_setItems_getSingleValues() {
    FeatureItem featureItem = new FeatureItem(1.0, 0.0, 0.0, 0.0);
    FeatureItem featureItem2 = new FeatureItem(1, 1, 1, 1);
    addAll(items, featureItem, featureItem2);
    adapter.setItemsTest(items);

    FeatureItem item1 = adapter.getItems().get(0);
    FeatureItem item2 = adapter.getItems().get(1);

    assertThat(item1.getItemFeatureOne(), is(doubleZero));
    assertThat(item1.getItemFeatureTwo(), is(doubleZero));
    assertThat(item1.getItemPredictedValue(), is(doubleZero));
    assertThat(item2.getItemFeatureOne(), is(doubleOne));
    assertThat(item2.getItemFeatureTwo(), is(doubleOne));
    assertThat(item2.getItemPredictedValue(), is(doubleOne));
  }

  @Test
  public void adapter_setItems_getAllValues() {
    FeatureItem featureItem = new FeatureItem(1.0, 0.0, 0.0, 0.0);
    FeatureItem featureItem2 = new FeatureItem(1, 1, 1, 1);
    addAll(items, featureItem, featureItem2);
    adapter.setItemsTest(items);

    FeatureItem item1 = adapter.getItemAtPosition(0);
    FeatureItem item2 = adapter.getItemAtPosition(1);

    double[] itemOneAllValues = item1.getAllvalues();
    double[] itemTwoAllValues = item2.getAllvalues();

    assertThat(itemOneAllValues[0], is(doubleOne));
    assertThat(itemOneAllValues[1], is(doubleZero));
    assertThat(itemOneAllValues[2], is(doubleZero));
    assertThat(itemOneAllValues[2], is(doubleZero));

    assertThat(itemTwoAllValues[0], is(doubleOne));
    assertThat(itemTwoAllValues[1], is(doubleOne));
    assertThat(itemTwoAllValues[2], is(doubleOne));
    assertThat(itemTwoAllValues[3], is(doubleOne));
  }

  @Test
  public void adapter_setItems_getItemValues() {
    FeatureItem featureItem = new FeatureItem(1.0, 0.0, 0.0, 0.0);
    FeatureItem featureItem2 = new FeatureItem(1, 1, 1, 1);
    addAll(items, featureItem, featureItem2);
    adapter.setItemsTest(items);

    FeatureItem item1 = adapter.getItemAtPosition(0);
    FeatureItem item2 = adapter.getItemAtPosition(1);

    double[] itemOneAllValues = item1.getItemValues();
    double[] itemTwoAllValues = item2.getItemValues();

    assertThat(itemOneAllValues[0], is(doubleZero));
    assertThat(itemOneAllValues[1], is(doubleZero));
    assertThat(itemOneAllValues[2], is(doubleZero));

    assertThat(itemTwoAllValues[0], is(doubleOne));
    assertThat(itemTwoAllValues[1], is(doubleOne));
    assertThat(itemTwoAllValues[2], is(doubleOne));
  }

  /*===============================*
   *   Extend addItem interface    *
   *===============================*/
  public void addItem(List<FeatureItem> list, FeatureItem featureItem) {
    list.add(featureItem);
  }

  public void addAll(List<FeatureItem> list, FeatureItem... featureItems) {
    Collections.addAll(list, featureItems);
  }

  public void setList(List<FeatureItem> source) {
    this.items = source;
  }
}
