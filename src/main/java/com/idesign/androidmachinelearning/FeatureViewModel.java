package com.idesign.androidmachinelearning;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

public class FeatureViewModel extends ViewModel {
  private MutableLiveData<List<FeatureItem>> items;

  public LiveData<List<FeatureItem>> getItems() {
    if (items == null) {
      items = new MutableLiveData<List<FeatureItem>>();
    }
    return items;
  }

  public void setModelItems(List<FeatureItem> incoming) {
    if (items == null) {
      items = new MutableLiveData<List<FeatureItem>>();
    }
    items.setValue(incoming);
  }

}
