package com.idesign.androidmachinelearning;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.FloatingActionButton;
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

  ImageButton addItemButton, removeItemButton, submitButton;
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
    addItemButton = findViewById(R.id.main_add_item_button);
    removeItemButton = findViewById(R.id.main_remove_item_button);

    recyclerView = findViewById(R.id.main_recycler_view);
    gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
    recyclerView.setLayoutManager(gridLayoutManager);

    items = new ArrayList<>();
    labels = new ArrayList<>();
    final FeatureItemAdapter adapter = new FeatureItemAdapter(items);
    recyclerView.setAdapter(adapter);

    viewModel = ViewModelProviders.of(this).get(FeatureViewModel.class);
    final Observer<List<FeatureItem>> itemObserver = featureItems -> {
      if (featureItems == null) {
        return;
      }
      for (FeatureItem item : featureItems) {
       if (!adapter.getItems().contains(item)) {
         int position = adapter.getItemCount();
         int j = position - 1;
         adapter.add(item, j);
       }
      }
    };
    viewModel.getItems().observe(this, itemObserver);

    addItemButton.setOnClickListener(l -> addItemToAdapter(adapter));

    removeItemButton.setOnClickListener(l -> removeItem(adapter));
    submitButton.setOnClickListener(l -> getAdapterValues(adapter));
  }

  public void addItemToAdapter(FeatureItemAdapter adapter) {
    adapter.add(new FeatureItem(1.0, 0.0, 0.0, 0.0), adapter.getItemCount());
  }

  public void removeItem(FeatureItemAdapter adapter) {
    int position = adapter.getItemCount();
    if (position == 0) {
      return;
    }
    adapter.removeItem(position - 1);
    viewModel.removeFeatureItem(position - 1);
  }

  public void getAdapterValues(FeatureItemAdapter adapter) {
    int size = adapter.getItemCount();
    boolean emptyValue = false;
    if (size == 0) {
      return;
    }
    for (int j = 0; j < size; j++) {
      View view = getView(j);
      FeatureItemAdapter.MyViewHolder vh = getViewHolder(view);
      FeatureItem item = adapter.items.get(j);
      vh.setValues(item);
      if (vh.emptyValue()) {
        emptyValue = true;
      }
    }

    viewModel.setFeatureItems(adapter.getItems());

    if (emptyValue) {
      return;
    }
    items = adapter.getItems();
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
    List<Double> labels = new ArrayList<>();
    for (FeatureItem featureItem : items) {
      dataSet.add(new Double[] {featureItem.getItemTheta(), featureItem.getItemFeatureOne(), featureItem.getItemFeatureTwo()});
      labels.add(featureItem.getItemPredictedValue());
    }

    Function<Double[], Double[]> scalingFunc = FeaturesScaling.createFunction(dataSet);
    List<Double[]> scaledDataSet = dataSet.stream().map(scalingFunc).collect(Collectors.toList());

    LinearRegressionFunction targetFunction = new LinearRegressionFunction(new double[] {1.0, 1.0, 1.0});
    for (int j = 0; j < 10000; j++) {
      targetFunction = Learner.train(targetFunction, scaledDataSet, labels, 0.1);
    }

    // placeholder values for prediction until UI added to generate dynamic values //
    // result ends up around $8615.********* at 10,000 iterations with values of 600.0, 360,000.0//

    Double[] scaledFeatureVector = scalingFunc.apply(new Double[] {1.0, valueOne, valueTwo});
    double predictedPrice = targetFunction.apply(scaledFeatureVector);
    resultView.setText(String.valueOf(predictedPrice));
  }

  public void showToast(CharSequence message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  public void logMessage(String message) {
    Log.d("Main activity", message);
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
