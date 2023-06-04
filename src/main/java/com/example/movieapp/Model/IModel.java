package com.example.movieapp.Model;

import java.util.List;

/**
 * Interface for any model
 */
public interface IModel {

  void add(IType m);

  List<IType> getMovies();

  List<IType> getWatchlist();

  void addW(IType m);

  void sortByName();

  void sortByRating();

  void sortByNameW();

  void watch(IType m);

  void remove(IType m);

  void rerate(IType movie, int rating);


}
