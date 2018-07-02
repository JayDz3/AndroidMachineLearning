package com.idesign.androidmachinelearning;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.idesign.androidmachinelearning.Interfaces.OnAddItem;
import com.idesign.androidmachinelearning.Interfaces.OnAddItems;
import com.idesign.androidmachinelearning.Interfaces.OnSetItems;

import java.util.Collections;
import java.util.List;

public class FeatureItemAdapter extends RecyclerView.Adapter<FeatureItemAdapter.MyViewHolder> implements
OnAddItems<FeatureItem>,
OnAddItem<FeatureItem>,
OnSetItems<FeatureItem> {

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

    public void setValues(FeatureItem featureItem) {
      featureItem.setFeatureOne(getValue(featureOneEditText));
      featureItem.setFeatureTwo(getValue(featureTwoEditText));
      featureItem.setPredictedValue(getValue(predictedValueEditText));
    }

    public void setValuesToZero(FeatureItem featureItem) {
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

  public void setItemsTest(List<FeatureItem> items) {
    this.items = items;
  }

  public List<FeatureItem> getItems() {
    return items;
  }

  public FeatureItem getItemAtPosition(int position) {
    return items.get(position);
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

  /*====================*
   * Override OnAddItem *
   *====================*/
  public void addItem(List<FeatureItem> items, FeatureItem featureItem) {
    items.add(featureItem);
  }

  public void addAllItems(List<FeatureItem> items, FeatureItem[] featureItems) {
    Collections.addAll(items, featureItems);
  }

  public void setItemsBy(List<FeatureItem> source) {
    this.items = source;
    notifyDataSetChanged();
  }
}
