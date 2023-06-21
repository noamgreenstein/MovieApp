package com.example.movieapp.Controller.gui;

import com.example.movieapp.Model.V1Model;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public abstract class AXML {

  protected FXMLLoader loader;
  protected V1Model model;
  protected Stage stage;

  public void setModel(V1Model model){
    this.model = model;
  }

  public void setStage(Stage stage){
    this.stage = stage;
  }

  public void setLoader(){
    this.loader = new FXMLLoader();
  }


}
