package com.example.movieapp.Controller;
import com.example.movieapp.Model.V1Model;
import com.example.movieapp.View.V1View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class AppProgram extends Application{

  public static void main(String[] args) {
   launch();
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader l = new FXMLLoader();
    l.setLocation(getClass().getClassLoader().getResource("home.fxml"));
    stage.setScene(l.load());
    V2Controller v = l.getController();
    v.setModel(new V1Model());
    v.setStage(stage);
    stage.show();
  }


}
