package com.example.movieapp.Controller.gui;

import com.example.movieapp.Controller.ChatGPT;
import com.example.movieapp.Controller.IMDB;
import com.example.movieapp.Model.IType;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class HomeController extends AXML{

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
  private String saveState;
  private String[] gptRecs;
  @FXML
  private Button loadButton;

  @FXML
  private Button mListButton;

  @FXML
  private Button recButton;

  @FXML
  private Button sanButton;

  @FXML
  private Button saveButton;

  @FXML
  private TextField searchBox;

  @FXML
  private Button wListButton;

  @FXML
  void generateRec(ActionEvent event) throws Exception {
    setLoader();
    loader.setLocation(getClass().getClassLoader().getResource("gpt.fxml"));
    Scene scene = loader.load();
    GPTController gpt = loader.getController();
    gpt.setModel(model);
    gpt.setStage(stage);
    if(Objects.isNull(gptRecs)){
      gptRecs = this.model.getGptRecs();
      gpt.setTitles(gptRecs);
    }
    else {
      gpt.setTitles(gptRecs);
    }
    gpt.init();
    stage.setScene(scene);
  }

  @FXML
  void load(ActionEvent event) throws IOException, SQLException {
    setLoader();
    loader.setLocation(getClass().getClassLoader().getResource("load.fxml"));
    Scene scene = loader.load();
    LoadController lc = loader.getController();
    lc.init();
    lc.setModel(model);
    lc.setStage(stage);
    saveState = lc.getSaveState();
    stage.setScene(scene);
  }

  @FXML
  void save(ActionEvent event) throws SQLException {
    if(Objects.isNull(saveState)) {
      saveAsNew(new ActionEvent());
    }
    else {
      saver();
    }
  }

  public void saver() throws SQLException {
    statement.execute(String.format("DELETE FROM %s;", saveState));
    for (IType t : model.getMovies()){
      statement.executeUpdate(String.format("INSERT INTO %s VALUES ('%s', '%s', %d, %d, %d);",
              saveState, t.getTitle(), t.getURL(), t.getYear(), t.getRating(), 1));
    }
    for (IType t : model.getWatchlist()){
      statement.executeUpdate(String.format("INSERT INTO %s VALUES ('%s', '%s', %d, %d, %d);",
              saveState, t.getTitle(), t.getURL(), t.getYear(), 0, 0));
    }
  }

  @FXML
  void saveAsNew(ActionEvent event) {
    Popup popup = new Popup();
    Group root = new Group();
    root.getChildren().add(new Label("Save As:"));
    TextField name = new TextField();
    root.getChildren().add(name);
    Button saver = new Button("Save");
    saver.setLayoutX(50);
    saver.setLayoutY(50);
    saver.setOnAction(arg0 -> {
      Statement st;
      try {
        st = connection.createStatement();
        String save;
        save = name.getText();
        saveState = save;
        st.execute(String.format("CREATE TABLE %s (title varchar(64), imgURL varchar(64), year int, rating int, seen int);", save));
        st.executeUpdate(String.format("INSERT INTO SAVES (refs) VALUES ('%s')", save));
        name.clear();
        popup.hide();
        saver();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    popup.getContent().add(saver);
    popup.getContent().add(root);
    popup.show(stage);
  }

  @FXML
  void search(ActionEvent event) throws IOException, SQLException, InterruptedException {
    setLoader();
    loader.setLocation(getClass().getClassLoader().getResource("imdb.fxml"));
    Scene scene = loader.load();
    IMDBController imdb = loader.getController();
    imdb.setModel(model);
    imdb.setStage(stage);
    String[] request = searchBox.getText().split(" ");
    imdb.setMovies(IMDB.request(request));
    imdb.init();
    searchBox.clear();
    stage.setScene(scene);
  }

  @FXML
  void showMList(ActionEvent event) throws IOException, SQLException {
    setLoader();
    loader.setLocation(getClass().getClassLoader().getResource("movielist.fxml"));
    Scene scene = loader.load();
    MovieListController ml = loader.getController();
    ml.setModel(model);
    ml.setStage(stage);
    ml.init();
    stage.setScene(scene);
  }

  @FXML
  void showWlist(ActionEvent event) throws IOException, SQLException {
    setLoader();
    loader.setLocation(getClass().getClassLoader().getResource("watchlist.fxml"));
    Scene scene = loader.load();
    WatchListController wl = loader.getController();
    wl.setModel(model);
    wl.setStage(stage);
    wl.init();
    stage.setScene(scene);
  }


}
