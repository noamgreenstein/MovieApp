package com.example.movieapp.Controller;
import com.example.movieapp.Model.IMDBMovie;
import com.example.movieapp.Model.IType;
import com.example.movieapp.Model.TYPES;
import com.example.movieapp.Model.V1Model;
import com.example.movieapp.View.V1View;
import com.example.movieapp.Model.IModel;
import com.example.movieapp.Model.Movie;
import com.example.movieapp.View.IView;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class V1Controller implements IController {

  IView view;
  IModel model;
  Button b;
  Button b2;

  Button b3;

  Button b4;

  Button backButton;

  public V1Controller(V1View view, V1Model model){
    this.view = view;
    this.model = model;
    this.backButton = new Button("Back");
  }


  public void setButton(Stage stage){
    b.setOnAction(arg0 -> view.sceneSet(stage, movieListScene(stage,"Movies Seen")));
    view.setLayout(b, 50, 50);
    backButton.setOnAction(arg0 -> {
      if (stage.getTitle().equals("Info for Watchlist")) {
        view.sceneSet(stage, movieListScene(stage,"Watchlist"));
      }
      else if (stage.getTitle().equals("Info for Movies Seen")){
        view.sceneSet(stage, movieListScene(stage,"Movies Seen"));
      }
      else {
        view.sceneSet(stage, initScene(stage));
        view.titleSet(stage, "Welcome");
      }
    });
    b3.setOnAction(arg0 -> {
      view.sceneSet(stage, movieListScene(stage, "Watchlist"));
      view.titleSet(stage, "Watchlist");
    });
    view.setLayout(b3, 50, 150);
    b4.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        view.sceneSet(stage, askForMovies(stage));
        view.titleSet(stage, "Add Movie");
      }
    });
    view.setLayout(b4,100,100);
  }

  public Scene movieListScene(Stage stage, String type) {
    stage.setTitle(type);
    boolean seen = type.equals("Movies Seen");
    Group root = new Group();
    ListView<String> list = new ListView<>();
    ObservableList<String> items = FXCollections.observableArrayList();
    list.setItems(items);
    list.setPrefWidth(100);
    list.setPrefHeight(200);
    view.setLayout(list,0,0);
    view.setLayout(backButton,0,250);
    root.getChildren().add(backButton);
    root.getChildren().add(list);
    if (seen) {
      for (IType m : model.getMovies()) {
        items.add(String.format("%s - %d", m.getTitle(), m.getRating()));
      }
      Button rr = new Button("Re-rate");
      view.setLayout(rr,150,250);
      root.getChildren().add(rr);
      rr.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
          try {
            int idx = list.getSelectionModel().getSelectedIndex();
            addRating(model.getMovies().get(idx),stage, TYPES.Movies);
            view.sceneSet(stage, movieListScene(stage,"Movies Seen"));
          } catch (IndexOutOfBoundsException e) {

          }
        }});
    } else {
      for (IType m : model.getWatchlist()) {
        items.add(String.format("%s", m.getTitle()));
      }
      watchlistOptions(stage, root, list);
    }
    Button info = new Button("Info");
    view.setLayout(info,50,250);
    root.getChildren().add(info);
    info.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        try {
          IType e;
          if(seen) {
            e = model.getMovies().get(list.getSelectionModel().getSelectedIndex());
          }
          else {
           e = model.getWatchlist().get(list.getSelectionModel().getSelectedIndex());
          }
          stage.setScene(infoScene(e));
          stage.setTitle(String.format("Info for %s", type));
        } catch (IndexOutOfBoundsException e) {
        }

      }
    });
    addSort(stage, root, model, type);
    return new Scene(root, 1000, 1000);
  }

  public void watchlistOptions(Stage s, Group r, ListView<String> list){
    Button w = new Button("Watch");
    Button rm = new Button("Remove");
    w.setLayoutY(150);
    w.setLayoutX(250);
    rm.setLayoutY(200);
    rm.setLayoutX(250);
    w.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        try {
          IType e = model.getWatchlist().get(list.getSelectionModel().getSelectedIndex());
          model.watch(e);
          addRating(e, s, TYPES.Watchlist);
        } catch (IndexOutOfBoundsException e) {
        }

      }
    });

    rm.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        try {
          IType e = model.getWatchlist().get(list.getSelectionModel().getSelectedIndex());
          model.remove(e);
          s.setScene(movieListScene(s, "Watchlist"));
        } catch (IndexOutOfBoundsException e) {
        }

      }
    });
    r.getChildren().add(w);
    r.getChildren().add(rm);
  }

  public void addRating(IType m, Stage s, TYPES t){
    Popup popup = new Popup();
    GridPane rootNode = new GridPane();
    rootNode.setPadding(new Insets(15));
    rootNode.setHgap(5);
    rootNode.setVgap(5);
    rootNode.setAlignment(Pos.CENTER);
    rootNode.add(new Label("Rating:"), 0, 0);
    TextField rating = new TextField();
    rootNode.add(rating, 1, 0);
    popup.getContent().add(rootNode);
    Button aButton = new Button("Add");
    rootNode.add(aButton, 1, 2);
    GridPane.setHalignment(aButton, HPos.LEFT);
    popup.show(s);
    aButton.setOnAction(e -> {
      try {
        model.rerate(m, Integer.parseInt(rating.getText()));
        popup.hide();
        if (t == TYPES.Movies) {
          s.setScene(movieListScene(s, "Movies Seen"));
        }
        else if(t == TYPES.Watchlist) {
          s.setScene(movieListScene(s, "Watchlist"));
        }
      } catch (NumberFormatException nfe) {
        System.out.println("improper input");
      }
    });

  }

  public void addSort(Stage s, Group r, IModel m, String type){
    Popup p = new Popup();
    p.setWidth(150);
    p.setHeight(150);
    Button a = new Button("Name");
    a.setLayoutY(0);
    a.setLayoutX(0);
    if (type.equals("Movies Seen")){
      Button b = new Button("Rating");
      b.setLayoutY(0);
      b.setLayoutX(50);
      b.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
          p.hide();
          m.sortByRating();
          s.setScene(movieListScene(s, type));
        }
      });
      p.getContent().add(b);
      a.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
          p.hide();
          m.sortByName();
          s.setScene(movieListScene(s, type));
        }
      });
      p.getContent().add(a);
    } else {
      a.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
          p.hide();
          m.sortByNameW();
          s.setScene(movieListScene(s, type));
        }
      });
      p.getContent().add(a);
    }
    Button sort = new Button("Sort By");
    sort.setLayoutY(100);
    sort.setLayoutX(230);
    r.getChildren().add(sort);
    sort.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent arg0) {
        p.show(s);
      }
    });
  }

  public Scene askForMovies(Stage s) {
    GridPane rootNode = new GridPane();
    rootNode.setPadding(new Insets(15));
    rootNode.setHgap(5);
    rootNode.setVgap(5);
    rootNode.setAlignment(Pos.CENTER);
    Scene myScene = new Scene(rootNode, 1000, 1000);
    rootNode.add(new Label("Title:"), 0, 0);
    TextField title = new TextField();
    rootNode.add(title, 1, 0);
    Button aButton = new Button("Search");
    rootNode.add(aButton, 1, 1);
    GridPane.setHalignment(aButton, HPos.LEFT);
    aButton.setOnAction(e -> {
      try {
        String[] movie = title.getText().split(" ");
        title.deleteText(0, title.getText().length());
        List<IMDBMovie> movies = IMDB.request(movie);
        if(movies != null){
          displaySearchResults(s, movies);
        }
      } catch (NumberFormatException nfe) {
        System.out.println("improper input");
        title.deleteText(0, title.getText().length());
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      } catch (InterruptedException ex) {
        throw new RuntimeException(ex);
      }
    });
    rootNode.add(backButton, 1, 3);
    GridPane.setHalignment(backButton, HPos.LEFT);
    setButton(s);
    return myScene;
  }

  public void displaySearchResults(Stage s, List<IMDBMovie> movies){
    Group root = new Group();
    ListView<ImageView> list = new ListView<>();
    ObservableList<ImageView> items = FXCollections.observableArrayList();
    list.setItems(items);
    list.setPrefWidth(500);
    list.setPrefHeight(500);
    view.setLayout(list,0,0);
    view.setLayout(backButton,0,750);
    root.getChildren().add(backButton);
    backButton.setOnAction(arg0 -> {
      s.setScene(askForMovies(s));
    });
    root.getChildren().add(list);
    for (IMDBMovie m : movies) {
      ImageView v = new ImageView(m.getImage());
      v.setFitWidth(250);
      v.setFitHeight(250);
      v.setX(0);
      v.setY(0);
      items.add(v);
    }
    Button addW = new Button("Add to Watchlist");
    addW.setOnAction(arg0 -> {
      model.addW(movies.get(list.getSelectionModel().getSelectedIndex()));
      s.setScene(askForMovies(s));
    });
    view.setLayout(addW,0,700);
    root.getChildren().add(addW);

    Button add = new Button("Watch");
    add.setOnAction(arg0 -> {
      model.add(movies.get(list.getSelectionModel().getSelectedIndex()));
      addRating((movies.get(list.getSelectionModel().getSelectedIndex())), s, TYPES.Search);
      s.setScene(askForMovies(s));
    });
    view.setLayout(add,0,650);
    root.getChildren().add(add);


    Scene scene = new Scene(root, 1000, 1000);
    s.setScene(scene);
  }
  @Override
  public void run(Stage stage) {
    Scene s = initScene(stage);
    stage.setScene(s);
    stage.show();
  }

  public Scene initScene(Stage stage) {
    stage.setTitle("Welcome");
    b = new Button("Movies");
    b3 = new Button("Watchlist");
    b4 = new Button("Add Movies");
    setButton(stage);
    Group g = new Group();
    g.getChildren().add(b);
    g.getChildren().add(b3);
    g.getChildren().add(b4);
    return new Scene(g, 1000, 1000);
  }

  @Override
  public void save(IType m) {

  }

  @Override
  public void load(String s) {

  }

  public Scene infoScene(IType m){
     Group g = new Group();
     Text t = new Text(String.format("Year: %d", m.getYear()));
     t.setLayoutY(50);
     g.getChildren().add(t);
     g.getChildren().add(backButton);
     return new Scene(g, 1000, 1000);
  }
}
