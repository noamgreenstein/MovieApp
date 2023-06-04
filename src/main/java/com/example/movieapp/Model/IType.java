package com.example.movieapp.Model;

/**
 * Interface for any viewable content
 */
public interface IType {

  String getTitle();
  void setRating(int rating);

  int getRating();

  int getYear();
}
