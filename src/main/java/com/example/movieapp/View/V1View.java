package com.example.movieapp.View;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class V1View implements IView{

  @Override
  public void display(String message) {
    System.out.println(message);
  }

  public void sceneSet(Stage stage, Scene scene){
    stage.setScene(scene);
  }

  public void setLayout(Node n, int x, int y){
    n.setLayoutX(x);
    n.setLayoutY(y);
  }

  public void titleSet(Stage stage, String title){
    stage.setTitle(title);
  }

}
