package com.example.movieapp.Controller.gui;

import com.example.movieapp.Controller.ChatGPT;
import com.example.movieapp.Controller.IMDB;
import com.example.movieapp.Model.IType;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class GPTController extends BACKXML{

  private String[] titles;

  @FXML
  private ListView<String> list;

  @FXML
  private Button newButton;

  @FXML
  private Button sButton;

  @Override
  public void init() throws SQLException {
    ObservableList<String> items = FXCollections.observableArrayList();
    for(String title : titles){
      items.add(title);
    }
    list.setItems(items);
  }

  @FXML
  void newList(ActionEvent event) throws Exception {
    setTitles(this.model.getGptRecs());
    init();
  }

  @FXML
  void search(ActionEvent event) throws IOException, InterruptedException, SQLException {
    setLoader();
    loader.setLocation(getClass().getClassLoader().getResource("imdb.fxml"));
    Scene scene = loader.load();
    IMDBController imdb = loader.getController();
    imdb.setModel(model);
    imdb.setStage(stage);
    String[] request = list.getSelectionModel().getSelectedItem().split(" ");
    imdb.setMovies(IMDB.request(request));
    imdb.init();
    stage.setScene(scene);
  }

  public void setTitles(String[] titles) {
    this.titles = titles;
  }

}
