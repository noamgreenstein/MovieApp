package com.example.movieapp.Model;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;

public class V1Model implements IModel{

  private List<IType> movies;
  private int size;

  private List<IType> watchlist;
  public V1Model(List<IType> movies) {
    this.movies = movies;
    this.size = movies.size();
    this.watchlist = new ArrayList<IType>();
  }

  public V1Model(List<IType> movies, List<IType> watchlist) {
    this.movies = movies;
    this.size = movies.size();
    this.watchlist = watchlist;
  }

  public V1Model(){
    this.movies = new ArrayList<IType>();
    this.size = 0;
    this.watchlist = new ArrayList<IType>();
  }

  @Override
  public void add(IType m) {
    movies.add(m);
    size += 1;
  }

  public void addW(IType m) {
    watchlist.add(m);
  }

  @Override
  public void sortByName() {
      this.movies.sort(new SortByName());
  }

  @Override
  public void sortByRating() {
    this.movies.sort(new SortByRating());
  }

  @Override
  public void sortByNameW() {
    this.watchlist.sort(new SortByName());
  }

  @Override
  public void watch(IType m) {
    this.movies.add(m);
    remove(m);
  }

  @Override
  public void remove(IType m) {
    this.watchlist.remove(m);
  }

  public void rerate(IType movie, int rating){
    int idx = this.movies.indexOf(movie);
    this.movies.get(idx).setRating(rating);
  }

  @Override
  public void clear() {
    this.movies = new ArrayList<>();
    this.watchlist = new ArrayList<>();
  }

  public List<IType> getMovies() {
    return this.movies;
  }

  public List<IType> getWatchlist() {
    return this.watchlist;
  }
}
