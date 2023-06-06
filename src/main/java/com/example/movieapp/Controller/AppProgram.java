package com.example.movieapp.Controller;
import com.example.movieapp.Model.V1Model;
import com.example.movieapp.View.V1View;

import javafx.application.Application;
import javafx.stage.Stage;

public class AppProgram extends Application{

  public static void main(String[] args) {
   launch();
  }

  @Override
  public void start(Stage stage) throws Exception {
    V1Controller c = new V1Controller(new V1View(), new V1Model());
    c.run(stage);
  }


}
