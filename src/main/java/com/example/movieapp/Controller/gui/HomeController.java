package com.example.movieapp.Controller.gui;

import com.example.movieapp.Controller.ChatGPT;
import com.example.movieapp.Model.IModel;
import com.example.movieapp.Model.IType;
import com.example.movieapp.Model.V1Model;

import java.util.Arrays;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HomeController {


  private IModel model;
  private Stage stage;

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
  void generateRec(ActionEvent event) {
//    if(Objects.isNull(gptRecs)){
//      gptRecs = getGptRecs();
//    }
//    s.setTitl`                                                                                        e("Recommendations");
//    Group g = new Group();
//    ListView<String> list = new ListView<>();
//    ObservableList<String> items = FXCollections.observableArrayList();
//    list.setItems(items);
//    list.setPrefWidth(500);
//    list.setPrefHeight(500);
//    view.setLayout(list,0,0);
//    backButton.setLayoutY(0);
//    backButton.setLayoutX(750);
//    g.getChildren().add(backButton);
//    backButton.setOnAction(arg0 -> {
//      s.setScene(initScene());
//    });
//    g.getChildren().add(list);
//    items.addAll(Arrays.asList(gptRecs));
//    Button select = new Button("Select");
//    select.setLayoutY(0);
//    select.setLayoutX(550);
//    g.getChildren().add(select);
//    select.setOnAction(actionEvent -> {
//      String title = list.getSelectionModel().getSelectedItem();
//      g.getChildren().remove(backButton);
//      try {
//        displaySearchResults(IMDB.request(title.split(" ")), chatGPT());
//      } catch (Exception e) {
//        throw new RuntimeException(e);
//      }
//
//    });
//    return new Scene(g, 1000, 1000);
  }

  @FXML
  void load(ActionEvent event) {
    //load scene -> load
  }

  @FXML
  void save(ActionEvent event) {
    // real save?
  }

  @FXML
  void saveAsNew(ActionEvent event) {
    // real save?
  }

  @FXML
  void search(ActionEvent event) {

  }

  @FXML
  void showMList(ActionEvent event) {

  }

  @FXML
  void showWlist(ActionEvent event) {

  }

  void setModel(V1Model model){
    this.model = model;
  }

  void setStage(Stage stage){
    this.stage = stage;
  }

  //CHANGE PROMPT
  private String[] getGptRecs() throws Exception {
    StringBuilder prompt = new StringBuilder();
    String provide = "Please provide me a movie recommendation list with movies found on IMDB. ";
    String format = "Please format the list by providing only the movie title and separate each title with a comma. ";
    String generate = "Please make your recommendation based off these following movies: ";
    prompt.append(provide);
    prompt.append(format);
    prompt.append(generate);
    for (IType m : model.getMovies()){
      prompt.append(String.format("%s, ", m.getTitle()));
    }
    String avoid = "Please do not include the following titles or the titles provided above: ";
    prompt.append(avoid);
    for (IType m : model.getWatchlist()){
      prompt.append(String.format("%s, ", m.getTitle()));
    }
    String res = "Please ONLY respond with the list of movies, no additional text";
    prompt.append(res);
    String response = ChatGPT.sendPrompt(prompt.toString()).substring(1);
    String[] recs = response.trim().split(",");

    return recs;
  }

}
