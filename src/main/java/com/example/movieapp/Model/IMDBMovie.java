package com.example.movieapp.Model;

import javafx.scene.image.Image;

public class IMDBMovie implements IType{
  private String title;
  private Image image;
  private int year;
  private int rating;


  public IMDBMovie(String title, String imgURL, int year, int rating) {
    this.title = title;
    try {
      this.image = new Image(imgURL);
    } catch (Exception e){
      this.image = null;
    }
    this.year = year;
    this.rating = rating;
  }

  public Image getImage() {
    return image;
  }

  @Override
  public String getTitle() {
    return this.title;
  }

  @Override
  public void setRating(int rating) {
    this.rating = rating;
  }

  @Override
  public int getRating() {
    return this.rating;
  }

  @Override
  public int getYear() {
    return this.year;
  }
}
