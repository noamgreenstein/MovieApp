package com.example.movieapp.Controller.gui;

import com.example.movieapp.Model.IType;
import com.example.movieapp.Model.TYPES;
import com.example.movieapp.Model.V1Model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;

public class MovieListController extends INFOXML{

  @FXML
  private Button infoButton;

  @FXML
  private Button rmButton;

  @FXML
  private Button rrButton;

  @FXML
  private Button sortButton;


  @FXML
  void sort(ActionEvent event) {
    Popup p = new Popup();
    p.setWidth(150);
    p.setHeight(150);
    Button a = new Button("Name");
    a.setLayoutY(0);
    a.setLayoutX(0);
    Button b = new Button("Rating");
    b.setLayoutY(0);
    b.setLayoutX(50);
      b.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
          model.sortByRating();
          init();
          p.hide();
        }
      });
      p.getContent().add(b);
      a.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
          model.sortByName();
          init();
          p.hide();
        }
      });
      p.getContent().add(a);
      p.show(stage);
  }

  @Override
  public void init(){
    refs = new HashMap<>();
    items = FXCollections.observableArrayList();
    list.setItems(items);
    for (IType m : model.getMovies()) {
      String view = String.format("%s - %d*", m.getTitle(), m.getRating());
      refs.put(view, m);
      items.add(view);
    }
  }
}
