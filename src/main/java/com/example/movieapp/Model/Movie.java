package com.example.movieapp.Model;
import java.util.List;

public class Movie implements IType{

  private String title;


  private double length;


  private String director;


  private int rating;

  public Movie(String title, int rating) {
    if (rating < 0 || rating > 10) {
      throw new IllegalArgumentException("invalid rating");
    }
    this.title = title;
    this .rating = rating;
    this.director = "";
    this.length = 0;
  }

  public String getTitle() {
    return title;
  }

  public int getRating() {
    return rating;
  }

  @Override
  public int getYear() {
    return 0;
  }

  public void setLength(double length){
    this.length = length;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDirector(String director) {
    this.director = director;
  }


  public void setRating(int rating) {
    this.rating = rating;
  }

  public double getLength() {
    return length;
  }

  public String getDirector() {
    return director;
  }
}
