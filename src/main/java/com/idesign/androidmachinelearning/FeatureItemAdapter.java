package com.idesign.androidmachinelearning;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

public class FeatureItemAdapter extends RecyclerView.Adapter<FeatureItemAdapter.MyViewHolder> {

  private List<FeatureItem> items;

  FeatureItemAdapter(List<FeatureItem> items) {
    this.items = items;
  }

  static class MyViewHolder extends RecyclerView.ViewHolder {
    private EditText featureOneEditText, featureTwoEditText, predictedValueEditText;

    MyViewHolder(View view) {
      super(view);
      featureOneEditText = view.findViewById(R.id.feature_layout_feature_one);
      featureTwoEditText = view.findViewById(R.id.feature_layout_feature_two);
      predictedValueEditText = view.findViewById(R.id.feature_layout_predicted_value);
    }

    public boolean emptyValue() {
      return TextUtils.isEmpty(featureOneEditText.getText().toString())
          || TextUtils.isEmpty(featureTwoEditText.getText().toString())
          || TextUtils.isEmpty(predictedValueEditText.getText().toString());
    }

    public void setValues(FeatureItem featureItem) {
      featureItem.setFeatureOne(getValue(featureOneEditText));
      featureItem.setFeatureTwo(getValue(featureTwoEditText));
      featureItem.setPredictedValue(getValue(predictedValueEditText));
    }

    public void setToZero(FeatureItem featureItem) {
      clearValues(featureItem);
      clearFields(featureOneEditText, featureTwoEditText, predictedValueEditText);
    }

    private void clearValues(FeatureItem featureItem) {
      featureItem.setFeatureOne(0.0);
      featureItem.setFeatureTwo(0.0);
      featureItem.setPredictedValue(0.0);
    }

    private void clearFields(EditText... editTexts) {
      for (EditText editText : editTexts) {
        editText.setText("");
      }
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
    final FeatureItem featureItem = items.get(position);
    if (featureItem != null) {
      screenFeatureItemValue(featureItem.getItemFeatureOne(), viewHolder.featureOneEditText);
      screenFeatureItemValue(featureItem.getItemFeatureTwo(), viewHolder.featureTwoEditText);
      screenFeatureItemValue(featureItem.getItemPredictedValue(), viewHolder.predictedValueEditText);
    }
    if (position == items.size() -1) {
      viewHolder.itemView.requestFocus();
    }
  }

  private void screenFeatureItemValue(double value, EditText editText) {
    if (value != 0.0) {
      setEditTextValue(editText, String.valueOf(value));
    }
  }

  private void setEditTextValue(EditText editText, String value) {
    editText.setText(value);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }
}
