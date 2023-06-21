package com.example.movieapp.Controller.gui;

import com.example.movieapp.Model.IType;

import java.sql.SQLException;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class WatchListController extends INFOXML{

  @FXML
  private Button backButton;

  @FXML
  private Button infoButton;

  @FXML
  private ListView<String> list;

  @FXML
  private Button rmButton;

  @FXML
  private Button wButton;

  @Override
  public void init() {
    refs = new HashMap<>();
    items = FXCollections.observableArrayList();
    list.setItems(items);
    for (IType m : model.getWatchlist()) {
      String view = String.format("%s", m.getTitle());
      refs.put(view, m);
      items.add(view);
    }
  }

  @FXML
  void watch(ActionEvent event) throws SQLException {
    IType m = refs.get(list.getSelectionModel().getSelectedItem());
    this.model.watch(m);
    rerate(new ActionEvent());
    remove(new ActionEvent());
  }

}

