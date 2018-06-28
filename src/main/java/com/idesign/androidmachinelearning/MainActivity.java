package com.idesign.androidmachinelearning;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.idesign.androidmachinelearning.MachineLearning.FeaturesScaling;
import com.idesign.androidmachinelearning.MachineLearning.Learner;
import com.idesign.androidmachinelearning.MachineLearning.LinearRegressionFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

  ImageButton addItemButton, removeItemButton, submitButton, resetButton;
  EditText predictionValueOne, predictionValueTwo;
  TextView resultView;
  RecyclerView recyclerView;

  List<FeatureItem> items;

  private FeatureViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setView();
    setItems();
    setClickListeners();

    final FeatureItemAdapter adapter = new FeatureItemAdapter(items);
    recyclerView.setAdapter(adapter);

    final Observer<List<FeatureItem>> itemObserver = featureItems -> setViewModel(adapter, featureItems);
    viewModel.getItems().observe(this, itemObserver);
  }

  public void setViewModel(FeatureItemAdapter adapter, List<FeatureItem> featureItems) {
    if (featureItems == null) {
      return;
    }
    items = featureItems;
    adapter.setItems(items);
  }


  public void setView() {
    resultView = findViewById(R.id.main_predicted_value_result);
    predictionValueOne = findViewById(R.id.main_predictor_value_one);
    predictionValueTwo = findViewById(R.id.main_predictor_value_two);

    submitButton = findViewById(R.id.main_submit_button);
    resetButton = findViewById(R.id.main_reset_button);
    addItemButton = findViewById(R.id.main_add_item_button);
    removeItemButton = findViewById(R.id.main_remove_item_button);

    recyclerView = findViewById(R.id.main_recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
  }

  public void setItems() {
    items = new ArrayList<>();
    viewModel = ViewModelProviders.of(this).get(FeatureViewModel.class);
  }

  public void setClickListeners() {
    addItemButton.setOnClickListener(l -> addItemToAdapter());
    removeItemButton.setOnClickListener(l -> removeItem());
    submitButton.setOnClickListener(l -> getAdapterValues());
    resetButton.setOnClickListener(l -> clearItems());
  }

  public boolean validPredictionValues() {
    return !TextUtils.isEmpty(predictionValueOne.getText().toString()) && !TextUtils.isEmpty(predictionValueTwo.getText().toString());
  }

  public boolean sizeIsZero(int size) {
    if (size == 0) {
      addFeatureItem();
      viewModel.setFeatureItems(items);
      return true;
    }
    return false;
  }

  public boolean isEmptyItem(FeatureItem featureItem) {
    return featureItem.getItemFeatureOne() == 0.0 || featureItem.getItemFeatureTwo() == 0.0 || featureItem.getItemPredictedValue() == 0.0;
  }

  public void clearViewFocus() {
    View focusedView = recyclerView.getFocusedChild();
    if (focusedView != null) {
      focusedView.clearFocus();
    }
  }

  public void addItemToAdapter() {
    int size = items.size();
    if (sizeIsZero(size)) {
      return;
    }
    setValuesAndClearView(size);
    FeatureItem featureItem = items.get(size - 1);
    if (isEmptyItem(featureItem)) {
      return;
    }
    addFeatureItem();
    viewModel.setFeatureItems(items);
  }

  public void setValuesAndClearView(int size) {
    clearViewFocus();
    for (int j = 0; j < size; j++) {
      FeatureItemAdapter.MyViewHolder vh = getViewHolder(j);
      vh.setValues(items.get(j));
    }
  }

  public void addFeatureItem() {
    items.add(new FeatureItem(1.0, 0.0, 0.0, 0.0));
  }

  public void removeItem() {
    final int size = items.size();
    if (size == 0) {
      return;
    }
    clearViewFocus();
    final int position = size - 1;
    resetViewHolder(position);
    items.remove(position);
    viewModel.setFeatureItems(items);
  }

  public void getAdapterValues() {
    clearTextViews(resultView);
    final int size = items.size();
    final int pos = size - 1;
    boolean emptyValue = false;
    if (size == 0) {
      return;
    }
    for (int j = 0; j < size; j++) {
      FeatureItemAdapter.MyViewHolder vh = getViewHolder(j);
      if (vh.emptyValue()) {
        emptyValue = true;
      }
    }
    if (emptyValue) {
      removeEmptyItem(pos);
    }
    if (items.size() == 0) {
      showToast("There is no valid data to assess");
      return;
    }
    if (validPredictionValues()) {
      double valueOne = Double.parseDouble(predictionValueOne.getText().toString());
      double valueTwo = Double.parseDouble(predictionValueTwo.getText().toString());
      runAlgorithm(valueOne, valueTwo);
    } else {
      showToast("Please fill out both predictor values");
    }
  }

  public void removeEmptyItem(int pos) {
    items.remove(pos);
    viewModel.setFeatureItems(items);
  }

  public void resetViewHolder(int position) {
    FeatureItemAdapter.MyViewHolder viewHolder = getViewHolder(position);
    viewHolder.setToZero(items.get(position));
  }

  public FeatureItemAdapter.MyViewHolder getViewHolder(int position) {
    return (FeatureItemAdapter.MyViewHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(position));
  }

  public void clearItems() {
    clearTextViews(resultView, predictionValueOne, predictionValueTwo);
    if (items.size() == 0) {
      return;
    }
    for (int j = 0; j < items.size(); j++) {
      resetViewHolder(j);
    }
    items.clear();
    viewModel.setFeatureItems(items);
  }

  public void clearTextViews(TextView... textViews) {
    for (TextView textView : textViews) {
      textView.setText("");
    }
  }

  /*==========================*
   *    Learning Algorithm    *
   *==========================*/


  /*===================================================*
   * Suggested Values                                  *
   *                                                   *
   * dataSet.add(new Double[] {1.0, 90.0, 8100.0});    *
   * dataSet.add(new Double[] {1.0, 101.0, 10201.0});  *
   * dataSet.add(new Double[] {1.0, 103.0, 10609.0});  *
   *                                                   *
   * labels.add(249.0);                                *
   * labels.add(338.0);                                *
   * labels.add(304.0);                                *
   *===================================================*/

  public void runAlgorithm(double valueOne, double valueTwo) {
    List<Double[]> dataSet = new ArrayList<>();
    List<Double> dataLabels = new ArrayList<>();

    int plotPoints;
    final int size = items.size();
    final int pos = size - 1;

    if (size < 1) {
      return;
    }
    if (isEmptyItem(items.get(pos))) {
      removeEmptyItem(pos);
    }
    if (items.size() < 1) {
      return;
    }

    addFeatureItemsToDataset(dataSet, dataLabels);
    plotPoints = dataLabels.size();
    logMessage("plot points: " + plotPoints);

    Function<Double[], Double[]> scalingFunc = FeaturesScaling.createFunction(dataSet);
    List<Double[]> scaledDataSet = dataSet.stream().map(scalingFunc).collect(Collectors.toList());
    LinearRegressionFunction targetFunction = new LinearRegressionFunction(new double[] {1.0, 1.0, 1.0});

    for (int j = 0; j < 10000; j++) {
      targetFunction = Learner.train(targetFunction, scaledDataSet, dataLabels, 0.1);
    }

    Double[] scaledFeatureVector = scalingFunc.apply(new Double[] {1.0, valueOne, valueTwo});
    double predictedPrice = targetFunction.apply(scaledFeatureVector);
    resultView.setText(String.valueOf(predictedPrice));
  }

  public void addFeatureItemsToDataset(List<Double[]> dataSet, List<Double> dataLabels) {
    for (FeatureItem featureItem : items) {
      dataSet.add(new Double[] { featureItem.getItemTheta(), featureItem.getItemFeatureOne(), featureItem.getItemFeatureTwo() });
      dataLabels.add(featureItem.getItemPredictedValue());
    }
  }

  public void logMessage(String message) {
    Log.d("Main Activity", message);
  }

  public void showToast(CharSequence message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }
}
