package com.example.movieapp.Controller.gui;

import com.example.movieapp.Model.IType;
import com.example.movieapp.Model.V1Model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public abstract class INFOXML extends BACKXML{
  @FXML
  protected ListView<String> list;

  protected HashMap<String,IType> refs;

  protected ObservableList<String> items;

  @FXML
  void remove(ActionEvent event) throws SQLException {
    IType m = refs.get(list.getSelectionModel().getSelectedItem());
    items.remove(list.getSelectionModel().getSelectedItem());
    model.remove(m);
    this.init();
  }

  @FXML
  void info(ActionEvent event) throws IOException {
    setLoader();
    loader.setLocation(getClass().getClassLoader().getResource("info.fxml"));
    Scene scene = loader.load();
    InfoController info = loader.getController();
    info.setModel(new V1Model());
    info.setStage(stage);
    info.setModel(model);
    info.setWatch(!list.getSelectionModel().getSelectedItem().contains("*"));
    IType movie = refs.get(list.getSelectionModel().getSelectedItem());
    info.setMovie(movie);
    info.init();
    stage.setScene(scene);

  }

  @FXML
  void rerate(ActionEvent event) {
    IType m = refs.get(list.getSelectionModel().getSelectedItem());
    Popup popup = new Popup();
    GridPane rootNode = new GridPane();
    rootNode.setPadding(new Insets(15));
    rootNode.setHgap(5);
    rootNode.setVgap(5);
    rootNode.setAlignment(Pos.CENTER);
    rootNode.add(new Label("Rating:"), 20, 3);
    TextField rating = new TextField();
    rootNode.add(rating, 20, 4);
    popup.getContent().add(rootNode);
    Button aButton = new Button("Add");
    rootNode.add(aButton, 20, 6);
    GridPane.setHalignment(aButton, HPos.LEFT);
    popup.show(stage);
    aButton.setOnAction(e -> {
      try {
        model.rerate(m, Integer.parseInt(rating.getText()));
        init();
        popup.hide();
      } catch (NumberFormatException nfe) {
        System.out.println("improper input");
      } catch (IndexOutOfBoundsException ioe){
        System.out.println("hello");
        rating.clear();
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
    });
  }

}
