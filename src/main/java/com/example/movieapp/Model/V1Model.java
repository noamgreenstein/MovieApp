package com.example.movieapp.Model;

import com.example.movieapp.Controller.ChatGPT;

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
  }

  @Override
  public void remove(IType m) {
    if(this.movies.contains(m) && !this.watchlist.contains(m)){
      this.movies.remove(m);
    }
    else {
      this.watchlist.remove(m);
    }
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

  public String[] getGptRecs() throws Exception {
    StringBuilder prompt = new StringBuilder();
    String provide = "Please provide me a movie recommendation list with movies found on IMDB. ";
    String format = "Please format the list by providing only the movie title and separate each title with a comma. ";
    String generate = "Please make your recommendation based off these following movies and their rating where the user rated the movie on a scale from 1 to 10: ";
    prompt.append(provide);
    prompt.append(format);
    prompt.append(generate);
    for (IType m : this.getMovies()){
      prompt.append(String.format("%s - %d, ", m.getTitle(), m.getRating()));
    }
    String avoid = "Please do not include the following titles or the titles provided above: ";
    prompt.append(avoid);
    for (IType m : this.getWatchlist()){
      prompt.append(String.format("%s, ", m.getTitle()));
    }
    String res = "Please ONLY respond with the list of movies, no additional text";
    prompt.append(res);
    String response = ChatGPT.sendPrompt(prompt.toString()).substring(1);
    String[] recs = response.trim().split(",");

    return recs;
  }
}
