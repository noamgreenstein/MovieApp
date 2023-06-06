package com.example.movieapp.Controller;

import com.example.movieapp.Model.IModel;
import com.example.movieapp.Model.IType;

import java.sql.SQLException;

import javafx.stage.Stage;

/**
 * Interface for any controller
 */
public interface IController {

  public void run(Stage stage);
  public void save() throws SQLException;
  public void load(String s) throws SQLException;
}
