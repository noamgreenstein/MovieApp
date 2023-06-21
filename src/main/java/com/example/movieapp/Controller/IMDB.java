package com.example.movieapp.Controller;
import com.example.movieapp.Model.IMDBMovie;

import java.io.IOException;
import java.net.http.HttpClient;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class IMDB {

  public static List<IMDBMovie> request(String[] title) throws IOException, InterruptedException {
    StringBuilder sb = new StringBuilder("https://moviesdatabase.p.rapidapi.com/titles/search/title/");
    sb.append(title[0]);
    try {
      for (int i = 1; i < title.length; i++) {
        sb.append("%20");
        sb.append(title[i]);
      }
    } catch (IndexOutOfBoundsException e){}
    sb.append("?exact=false&titleType=movie");
    String link = sb.toString();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(link))
            .header("X-RapidAPI-Key", "d3bdd5861bmsh09c9ecf4c6dbf83p1aae52jsn9e53121c18ef")
            .header("X-RapidAPI-Host", "moviesdatabase.p.rapidapi.com")
            .method("GET", HttpRequest.BodyPublishers.noBody())
            .build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    String r = response.body();
    List<IMDBMovie> movies = new ArrayList<>();
    String[] res = splitUp(r);
    if (res != null) {
      String[] titles = getTitles(res);
      String[] urls = getUrls(res);
      int[] years = getYear(res);
      for (int i = 0; i < res.length; i++) {
        movies.add(new IMDBMovie(titles[i],urls[i],years[i],0));
      }
      return movies;
    }
    System.out.println("huh");
    return null;
  }

  private static String[] splitUp(String response) {
    if (response.contains("\"entries\"")){
      String entries = response.substring(response.indexOf("\"entries\"") + 10, response.indexOf("\"results\"")-1);
      int e = Integer.parseInt(entries);
      if (e > 0){
        String[] result = new String[e];
        String movies = response.substring(response.indexOf(":["));
        String t = "\\},\\{";
        result = movies.split(t);
        return result;
      }
      else {
        return null;
      }
    }
    else {
      throw new RuntimeException(response);
    }
  }

  private static String[] getTitles(String[] movies){
    String[] titles = new String[movies.length];
    int i = 0;
    for( String s : movies) {
      titles[i] = s.substring(s.indexOf("\"titleText\":{\"text\":\"") + 21, s.indexOf("\",\"__typename\":\"TitleText\""));
      i++;
    }
    return titles;
  }

  private static String[] getUrls(String[] movies){
    String[] url = new String[movies.length];
    int i = 0;
    for( String s : movies) {
      int idx = s.indexOf("\"primaryImage\":") + 15;
      if(s.substring(idx, idx + 4).equals("null")) {
        url[i] = "null";
      } else {
        url[i] = s.substring(s.indexOf("\"url\":\"") + 7, s.indexOf("\"caption\""));
      }
      i++;
    }
    return url;
  }

  private static int[] getYear(String[] movies){
    int[] years = new int[movies.length];
    int i = 0;
    for( String s : movies) {
      int idx = s.indexOf("\"releaseYear\":") + 14;
      if(s.substring(idx, idx + 4).equals("null")) {
        years[i] = 0;
      } else {
        int index = s.indexOf(":{\"year\":") + 9;
        String y = s.substring(index, index+4);
        years[i] = Integer.parseInt(y);
      }
      i++;
    }
    return years;
  }

}
