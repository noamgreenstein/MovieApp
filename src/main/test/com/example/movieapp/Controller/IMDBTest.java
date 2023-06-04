package com.example.movieapp.Controller;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class IMDBTest {

  @Test
  void request() throws IOException, InterruptedException {
    String[] a = new String[2];
    a[0] = "toy";
    a[1] = "story";
    //String s = IMDB.request(a);
    //assertEquals(s,"9");

    String[] b = new String[2];
    b[0] = "cool";
    b[1] = "movie";
    //String st = IMDB.request(b);
    //assertEquals(st,null);
  }
}