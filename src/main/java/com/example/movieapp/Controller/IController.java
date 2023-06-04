package com.example.movieapp.Controller;

import com.example.movieapp.Model.IModel;
import com.example.movieapp.Model.IType;

import javafx.stage.Stage;

/**
 * Interface for any controller
 */
public interface IController {

  public void run(Stage stage);
  public void save(IType m);
  public void load(String s);
}
