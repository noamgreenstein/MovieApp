package com.example.movieapp.Controller.gui;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public abstract class BACKXML extends AXML{
  @FXML
  private Button backButton;

  @FXML
  void back(ActionEvent event) throws IOException {
    FXMLLoader l = new FXMLLoader();
    l.setLocation(getClass().getClassLoader().getResource("home.fxml"));
    Scene scene = l.load();
    HomeController home = l.getController();
    home.setModel(model);
    home.setStage(stage);
    stage.setScene(scene);
  }

  public abstract void init() throws SQLException;

}
