package com.example.movieapp.Model;

import java.util.Comparator;

public class SortByRating implements Comparator<IType> {

  @Override
  public int compare(IType o1, IType o2) {
    return -1 * (o1.getRating() - o2.getRating());
  }
}
