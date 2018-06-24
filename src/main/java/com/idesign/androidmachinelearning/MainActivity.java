package com.idesign.androidmachinelearning;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  Button submitButton;
  ImageButton addItemButton, removeItemButton;
  RecyclerView recyclerView;
  GridLayoutManager gridLayoutManager;

  List<FeatureItem> items;
  List<Double> labels;

  private FeatureViewModel viewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    submitButton = findViewById(R.id.main_submit);
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

    addItemButton.setOnClickListener(l -> addItem(adapter));
    removeItemButton.setOnClickListener(l -> removeItem(adapter));
    submitButton.setOnClickListener(l -> getAdapterValues(adapter));

  }

  public void addItem(FeatureItemAdapter adapter) {
    adapter.add(new FeatureItem(1.0, 0.0, 0.0, 0.0), adapter.getItemCount());
  }

  public void removeItem(FeatureItemAdapter adapter) {
    int position = adapter.getItemCount();
    if (position == 0) {
      return;
    }
    adapter.removeItem(position -1);
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

    viewModel.setModelItems(adapter.getItems());
    if (emptyValue) {
      return;
    }
    items = adapter.getItems();
  }

  public View getView(int position) {
    return recyclerView.getLayoutManager().getChildAt(position);
  }

  public FeatureItemAdapter.MyViewHolder getViewHolder(View view) {
    return (FeatureItemAdapter.MyViewHolder) recyclerView.getChildViewHolder(view);
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
