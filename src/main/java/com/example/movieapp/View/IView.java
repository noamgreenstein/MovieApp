package com.example.movieapp.View;
import com.example.movieapp.Model.Movie;

import java.util.List;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Interface for any view
 */
public interface IView {

  void display(String message);
  void sceneSet(Stage stage, Scene scene);

  void setLayout(Node n, int x, int y);

  void titleSet(Stage stage, String title);


}
