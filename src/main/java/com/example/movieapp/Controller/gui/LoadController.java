package com.example.movieapp.Controller.gui;

import com.example.movieapp.Model.IMDBMovie;
import com.example.movieapp.Model.IType;
import com.example.movieapp.Model.V1Model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class LoadController extends BACKXML{

  private String saveState;
  private Connection connection;

  {
    try {
      connection = DriverManager.getConnection("jdbc:sqlite:/Users/noamgreenstein/Documents/Projects/MovieApp/src/db");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  Statement statement;

  {
    try {
      statement = connection.createStatement();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  ResultSet rs;

  @FXML
  private ListView<String> list;

  @FXML
  private Button loader;

  @FXML
  void load(ActionEvent event) throws IOException, SQLException {
    String v = list.getSelectionModel().getSelectedItem();
    this.saveState = v;
    rs = statement.executeQuery(String.format("SELECT * FROM %s;", v));
    List<IType> newMovies = new ArrayList<>();
    List<IType> newWL = new ArrayList<>();
    while (rs.next()){
      if (rs.getInt("seen") == 1){
        newMovies.add(new IMDBMovie(rs.getString("title"),
                rs.getString("imgURL"),
                rs.getInt("year"),
                rs.getInt("rating")));
      }
      else {
        newWL.add(new IMDBMovie(rs.getString("title"),
                rs.getString("imgURL"),
                rs.getInt("year"),
                rs.getInt("rating")));
      }
    }
    model = new V1Model(newMovies, newWL);
    back(new ActionEvent());
  }

  @Override
  public void init() throws SQLException {
    ObservableList<String> items = FXCollections.observableArrayList();
    list.setItems(items);
    ResultSet rs = statement.executeQuery("SELECT * FROM SAVES;");
    while (rs.next()){
      items.add(rs.getString("refs"));
    }
  }

  public String getSaveState() {
    return saveState;
  }
}
