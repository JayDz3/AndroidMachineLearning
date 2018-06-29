package com.idesign.androidmachinelearning.Interfaces;

import java.util.List;

public interface OnAddItem<T> {
  void addItem(List<T> list, T t);

}
