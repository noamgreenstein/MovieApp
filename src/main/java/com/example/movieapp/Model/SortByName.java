package com.example.movieapp.Model;

import java.util.Comparator;

public class SortByName implements Comparator<IType> {
  @Override
  public int compare(IType o1, IType o2) {
    return o1.getTitle().compareToIgnoreCase(o2.getTitle());
  }
}
