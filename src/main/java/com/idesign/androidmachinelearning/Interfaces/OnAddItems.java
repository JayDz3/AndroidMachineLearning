package com.idesign.androidmachinelearning.Interfaces;

import java.util.List;

public interface OnAddItems<T> {
  void addAllItems(List<T> list, T[] ts);
}
