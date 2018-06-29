package com.idesign.androidmachinelearning;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.idesign.androidmachinelearning.Interfaces.OnSetItems;

import java.util.ArrayList;
import java.util.List;

public class FeatureViewModel extends ViewModel implements OnSetItems<FeatureItem> {
  private MutableLiveData<List<FeatureItem>> items;

  public LiveData<List<FeatureItem>> getItems() {
    if (items == null) {
      items = new MutableLiveData<>();
      items.setValue(new ArrayList<>());
    }
    return items;
  }

  public void setItemsBy(List<FeatureItem> incoming) {
    if (items == null) {
      items = new MutableLiveData<>();
    }
    items.setValue(incoming);
  }
}
