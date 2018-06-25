package com.idesign.androidmachinelearning;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
  GridLayoutManager gridLayoutManager;

  List<FeatureItem> items;
  List<Double> labels;

  private FeatureViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    resultView = findViewById(R.id.main_predicted_value_result);
    predictionValueOne = findViewById(R.id.main_predictor_value_one);
    predictionValueTwo = findViewById(R.id.main_predictor_value_two);

    submitButton = findViewById(R.id.main_submit_button);
    resetButton = findViewById(R.id.main_reset_button);
    addItemButton = findViewById(R.id.main_add_item_button);
    removeItemButton = findViewById(R.id.main_remove_item_button);

    recyclerView = findViewById(R.id.main_recycler_view);
    gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
    recyclerView.setLayoutManager(gridLayoutManager);

    labels = new ArrayList<>();
    items = new ArrayList<>();
    viewModel = ViewModelProviders.of(this).get(FeatureViewModel.class);

    final FeatureItemAdapter adapter = new FeatureItemAdapter(items);
    recyclerView.setAdapter(adapter);

    final Observer<List<FeatureItem>> itemObserver = featureItems -> {
      if (featureItems == null) {
        return;
      }
      items = featureItems;
      adapter.setItems(items);
    };
    viewModel.getItems().observe(this, itemObserver);

    addItemButton.setOnClickListener(l -> addItemToAdapter());

    removeItemButton.setOnClickListener(l -> removeItem());
    submitButton.setOnClickListener(l -> getAdapterValues());
    resetButton.setOnClickListener(l -> clear());
  }

  public void addItemToAdapter() {
    if (items.size() > 0) {
      int size = items.size();
      View v = recyclerView.getFocusedChild();
      if (v != null) {
        v.clearFocus();
      }
      for (int j = 0; j < size; j++) {
        View view = getView(j);
        FeatureItemAdapter.MyViewHolder vh = getViewHolder(view);
        vh.setValues(items.get(j));
      }
      FeatureItem featureItem = items.get(size - 1);
      if (featureItem.getItemFeatureOne() == 0.0 || featureItem.getItemFeatureTwo() == 0.0 || featureItem.getItemPredictedValue() == 0.0) {
        return;
      }
      items.add(new FeatureItem(1.0, 0.0, 0.0, 0.0));
      viewModel.setFeatureItems(items);
    } else {
      items.add(new FeatureItem(1.0, 0.0, 0.0, 0.0));
      viewModel.setFeatureItems(items);
    }
  }

  public void removeItem() {
    int size = items.size();
    if (size == 0) {
      return;
    }
    View view = recyclerView.getFocusedChild();
    if (view != null) {
      view.clearFocus();
    }
    int position = size - 1;
    resetViewHolder(position);
    items.remove(position);
    viewModel.setFeatureItems(items);
  }

  public void resetViewHolder(int position) {
    View view = getView(position);
    FeatureItemAdapter.MyViewHolder viewHolder = getViewHolder(view);
    viewHolder.setToZero(items.get(position));
  }

  public void getAdapterValues() {
    resultView.setText("");
    int size = items.size();
    boolean emptyValue = false;
    if (size == 0) {
      return;
    }
    for (int j = 0; j < size; j++) {
      View view = getView(j);
      FeatureItemAdapter.MyViewHolder vh = getViewHolder(view);
      if (vh.emptyValue()) {
        emptyValue = true;
      }
    }
    if (emptyValue) {
      int pos = size - 1;
      items.remove(pos);
      viewModel.setFeatureItems(items);
    }
    if (validPredictionValues()) {
      double valueOne = Double.parseDouble(predictionValueOne.getText().toString());
      double valueTwo = Double.parseDouble(predictionValueTwo.getText().toString());
      runAlgorithm(valueOne, valueTwo);
    } else {
      showToast("Please fill out both predictor values");
    }
  }

  public View getView(int position) {
    return recyclerView.getLayoutManager().getChildAt(position);
  }

  public FeatureItemAdapter.MyViewHolder getViewHolder(View view) {
    return (FeatureItemAdapter.MyViewHolder) recyclerView.getChildViewHolder(view);
  }

  public boolean validPredictionValues() {
    return !TextUtils.isEmpty(predictionValueOne.getText().toString()) && !TextUtils.isEmpty(predictionValueTwo.getText().toString());
  }

  public void clear() {
    resultView.setText("");
    predictionValueOne.setText("");
    predictionValueTwo.setText("");
    if (items.size() > 0) {
      for (int j = 0; j < items.size(); j++) {
       resetViewHolder(j);
      }
      items.clear();
      viewModel.setFeatureItems(items);
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

  public boolean isEmptyItem(FeatureItem featureItem) {
    return featureItem.getItemFeatureOne() == 0.0 || featureItem.getItemFeatureTwo() == 0.0 || featureItem.getItemPredictedValue() == 0.0;
  }

  public void runAlgorithm(double valueOne, double valueTwo) {
    List<Double[]> dataSet = new ArrayList<>();
    List<Double> labels = new ArrayList<>();

    int plotPoints = 0;
    int size = items.size();
    if (size < 1) {
      return;
    }
    int pos = size - 1;
    FeatureItem emptyItem = items.get(pos);
    if (isEmptyItem(emptyItem)) {
      items.remove(emptyItem);
      viewModel.setFeatureItems(items);
    }
    if (items.size() < 1) {
      return;
    }

    for (FeatureItem featureItem : items) {
      dataSet.add(new Double[] {featureItem.getItemTheta(), featureItem.getItemFeatureOne(), featureItem.getItemFeatureTwo()});
      labels.add(featureItem.getItemPredictedValue());
    }
    plotPoints = labels.size();
    for (int j = 0; j < plotPoints; j++) {
      Log.d("MAIN", "Plot point # " + j + " Label: " + labels.get(j));
    }

    Function<Double[], Double[]> scalingFunc = FeaturesScaling.createFunction(dataSet);
    List<Double[]> scaledDataSet = dataSet.stream().map(scalingFunc).collect(Collectors.toList());
    LinearRegressionFunction targetFunction = new LinearRegressionFunction(new double[] {1.0, 1.0, 1.0});

    for (int j = 0; j < 10000; j++) {
      targetFunction = Learner.train(targetFunction, scaledDataSet, labels, 0.1);
    }

    Double[] scaledFeatureVector = scalingFunc.apply(new Double[] {1.0, valueOne, valueTwo});
    double predictedPrice = targetFunction.apply(scaledFeatureVector);
    resultView.setText(String.valueOf(predictedPrice));
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
