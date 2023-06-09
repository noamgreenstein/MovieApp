package com.example.movieapp.Controller.gui;
import com.example.movieapp.Model.IMDBMovie;
import com.example.movieapp.Model.IType;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;

public class IMDBController extends BACKXML{

  private List<IMDBMovie> movies;
  private HashMap<ImageView, IMDBMovie> refs;

  @FXML
  private ListView<ImageView> list;

  @FXML
  private Button wButton;

  @FXML
  private Button wlistButton;

  @FXML
  void addW(ActionEvent event) {
    ImageView v = list.getSelectionModel().getSelectedItem();
    IType m = refs.get(v);
    this.model.addW(m);
  }

  @Override
  public void init() throws SQLException {
    refs = new HashMap<>();
    ObservableList<ImageView> items = FXCollections.observableArrayList();
    for (IMDBMovie m : movies) {
      if(!Objects.isNull(m.getImage())){
        ImageView v = new ImageView(m.getImage());
        refs.put(v,m);
        v.setFitWidth(100);
        v.setFitHeight(100);
        items.add(v);
      }
    }
    this.list.setItems(items);
  }

  @FXML
  void watch(ActionEvent event) {
    ImageView v = list.getSelectionModel().getSelectedItem();
    IType m = refs.get(v);
    this.model.add(m);
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

  public void setMovies(List<IMDBMovie> movies){
    this.movies = movies;
  }

}

