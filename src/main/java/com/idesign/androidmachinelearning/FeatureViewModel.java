package com.idesign.androidmachinelearning;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class FeatureViewModel extends ViewModel {
  private MutableLiveData<List<FeatureItem>> items;

  public LiveData<List<FeatureItem>> getItems() {
    if (items == null) {
      items = new MutableLiveData<>();
    }
    return items;
  }

  public void setFeatureItems(List<FeatureItem> incoming) {
    if (items == null) {
      items = new MutableLiveData<>();
    }
    items.setValue(incoming);
  }

  public void addFeatureItem(FeatureItem featureItem) {
    if (items == null) {
      items = new MutableLiveData<>();
    }
    if (items.getValue() != null) {
      items.getValue().add(featureItem);
    }
  }

  public void removeFeatureItem(int position) {
    if (items == null) {
      return;
    }
    if (items.getValue() != null) {
      if (items.getValue().get(position) != null) {
        items.getValue().remove(position);
      }
    }
  }
}
