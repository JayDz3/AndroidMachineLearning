package com.idesign.androidmachinelearning;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

public class FeatureItemAdapter extends RecyclerView.Adapter<FeatureItemAdapter.MyViewHolder> {

  List<FeatureItem> items;

  FeatureItemAdapter(List<FeatureItem> items) {
    this.items = items;
  }

  static class MyViewHolder extends RecyclerView.ViewHolder {
    EditText featureOne, featureTwo, predictedValue;

    MyViewHolder(View view) {
      super(view);
      featureOne = view.findViewById(R.id.feature_layout_feature_one);
      featureTwo = view.findViewById(R.id.feature_layout_feature_two);
      predictedValue = view.findViewById(R.id.feature_layout_predicted_value);
    }

    public boolean emptyValue() {
      return TextUtils.isEmpty(featureOne.getText().toString())
      || TextUtils.isEmpty(featureTwo.getText().toString())
      || TextUtils.isEmpty(predictedValue.getText().toString());
    }

    public void setValues(FeatureItem item) {
      double v1 = getValue(featureOne);
      double v2 = getValue(featureTwo);
      double val = getValue(predictedValue);
      item.setFeatureOne(v1);
      item.setFeatureTwo(v2);
      item.setPredictedValue(val);
    }

    public void setToZero(FeatureItem featureItem) {
      featureItem.setFeatureOne(0.0);
      featureItem.setFeatureTwo(0.0);
      featureItem.setPredictedValue(0.0);
      featureOne.setText("");
      featureTwo.setText("");
      predictedValue.setText("");
    }

    private double getValue(EditText editText) {
      if (TextUtils.isEmpty(editText.getText().toString()))
        return 0.0;
      else
        return Double.parseDouble(editText.getText().toString());
    }
  }

  public void setItems(List<FeatureItem> items) {
    this.items = items;
    notifyDataSetChanged();
  }

  @Override
  @NonNull
  public FeatureItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feature_layout, parent, false);
    return new MyViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull FeatureItemAdapter.MyViewHolder viewHolder, final int position) {
    FeatureItem featureItem = items.get(position);
    if (featureItem != null) {
      if (featureItem.getItemFeatureOne() != 0.0)
      viewHolder.featureOne.setText(String.valueOf(featureItem.getItemFeatureOne()));
      if (featureItem.getItemFeatureTwo() != 0.0)
      viewHolder.featureTwo.setText(String.valueOf(featureItem.getItemFeatureTwo()));
      if (featureItem.getItemPredictedValue() != 0.0)
      viewHolder.predictedValue.setText(String.valueOf(featureItem.getItemPredictedValue()));
    }
    viewHolder.itemView.requestFocus();
  }

  @Override
  public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
    Log.d("ADAPTER", "DETACH FROM RECYCLER VIEW");
  }

  @Override
  public int getItemCount() {
    return items.size();
  }
}
