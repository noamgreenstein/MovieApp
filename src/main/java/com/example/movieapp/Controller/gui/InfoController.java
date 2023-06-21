package com.example.movieapp.Controller.gui;

import com.example.movieapp.Model.IType;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class InfoController extends BACKXML{

  private boolean watch;
  private IType movie;
  @FXML
  private Button backButton;

  @FXML
  private Text rating;

  @FXML
  private Text title;

  @FXML
  private ImageView view;

  @FXML
  private Text year;

  @Override
  void back(ActionEvent event) throws IOException {
    setLoader();
    Scene scene;

    if(watch){
      loader.setLocation(getClass().getClassLoader().getResource("watchlist.fxml"));
      scene = loader.load();
      WatchListController wl = loader.getController();
      wl.setModel(model);
      wl.setStage(stage);
      wl.init();
    }
    else {
      loader.setLocation(getClass().getClassLoader().getResource("movielist.fxml"));
      scene = loader.load();
      MovieListController ml = loader.getController();
      ml.setModel(model);
      ml.setStage(stage);
      ml.init();
    }

    stage.setScene(scene);
  }

  @Override
  public void init(){
    this.title.setText(this.title.getText() + String.format(" %s", movie.getTitle()));
    if(this.movie.getYear() != 0) {
      this.rating.setText(this.rating.getText() + String.format(" %d", movie.getRating()));
    }
    else {
      this.rating.setText(this.rating.getText() + " N/A");
    }
    this.year.setText(this.year.getText() + String.format(" %d", movie.getYear()));
    this.view.setImage(new Image(this.movie.getURL()));
  }

  public void setMovie(IType movie){
    this.movie = movie;
  }

  public void setWatch(boolean watch){
    this.watch = watch;
  }
}
