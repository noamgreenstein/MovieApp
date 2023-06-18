package com.example.movieapp.Controller;
import com.example.movieapp.Model.IMDBMovie;
import com.example.movieapp.Model.IType;
import com.example.movieapp.Model.TYPES;
import com.example.movieapp.Model.V1Model;
import com.example.movieapp.View.V1View;
import com.example.movieapp.Model.IModel;
import com.example.movieapp.View.IView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
  Button b5;
  Button b6;
  Button b7;

  Button backButton;

  Connection connection;
  String currModel;

  Stage s;
  
  String[] gptRecs;

  public V1Controller(V1View view, V1Model model) throws SQLException {
    this.view = view;
    this.model = model;
    this.backButton = new Button("Back");
    this.connection = DriverManager.getConnection("jdbc:sqlite:/Users/noamgreenstein/Documents/Projects/MovieApp/src/db");
    this.currModel = "";
  }


  public void setButton(){
    b.setOnAction(arg0 -> view.sceneSet(this.s, movieListScene("Movies Seen")));
    view.setLayout(b, 50, 50);
    backButton.setOnAction(arg0 -> {
      if (this.s.getTitle().equals("Info for Watchlist")) {
        view.sceneSet(this.s, movieListScene("Watchlist"));
      }
      else if (this.s.getTitle().equals("Info for Movies Seen")){
        view.sceneSet(this.s, movieListScene("Movies Seen"));
      }
      else {
        view.sceneSet(this.s, initScene());
        view.titleSet(this.s, "Welcome");
      }
    });
    b2.setOnAction(arg0 -> {
      try {
        realSave();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    view.setLayout(b2,500,500);
    b3.setOnAction(arg0 -> {
      view.sceneSet(this.s, movieListScene("Watchlist"));
      view.titleSet(this.s, "Watchlist");
    });
    view.setLayout(b3, 50, 150);
    b4.setOnAction(arg0 -> {
      view.sceneSet(s, askForMovies());
      view.titleSet(s, "Add Movie");
    });
    view.setLayout(b4,100,100);
    b5.setOnAction(arg0 ->{
      try {
        loadScene();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    view.setLayout(b5,500,550);
    b6.setOnAction(arg0 ->{
      try {
        newSaveState();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    view.setLayout(b6,500,600);
    b7.setOnAction(arg0 -> {
      try {
        s.setScene(chatGPT());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    view.setLayout(b7, 50, 200);
  }

  public Scene movieListScene(String type) {
    this.s.setTitle(type);
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
            addRating(model.getMovies().get(idx), TYPES.Movies);
            view.sceneSet(s, movieListScene("Movies Seen"));
          } catch (IndexOutOfBoundsException e) {

          }
        }});
    } else {
      for (IType m : model.getWatchlist()) {
        items.add(String.format("%s", m.getTitle()));
      }
      watchlistOptions(root, list);
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
          s.setScene(infoScene(e));
          s.setTitle(String.format("Info for %s", type));
        } catch (IndexOutOfBoundsException e) {
        }

      }
    });
    addSort(root, model, type);
    return new Scene(root, 1000, 1000);
  }

  public void watchlistOptions(Group r, ListView<String> list){
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
          addRating(e, TYPES.Watchlist);
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
          s.setScene(movieListScene("Watchlist"));
        } catch (IndexOutOfBoundsException e) {
        }

      }
    });
    r.getChildren().add(w);
    r.getChildren().add(rm);
  }

  public void addRating(IType m, TYPES t){
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
          s.setScene(movieListScene("Movies Seen"));
        }
        else if(t == TYPES.Watchlist) {
          s.setScene(movieListScene("Watchlist"));
        }
      } catch (NumberFormatException nfe) {
        System.out.println("improper input");
      }
    });

  }

  public void addSort(Group r, IModel m, String type){
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
          s.setScene(movieListScene(type));
        }
      });
      p.getContent().add(b);
      a.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
          p.hide();
          m.sortByName();
          s.setScene(movieListScene(type));
        }
      });
      p.getContent().add(a);
    } else {
      a.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent arg0) {
          p.hide();
          m.sortByNameW();
          s.setScene(movieListScene(type));
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

  public Scene askForMovies() {
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
          displaySearchResults(movies, askForMovies());
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
    backButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
        public void handle(ActionEvent actionEvent) {
      s.setScene(initScene());
      }
    });
    rootNode.add(backButton, 1, 3);
    GridPane.setHalignment(backButton, HPos.LEFT);
    setButton();
    return myScene;
  }

  public void displaySearchResults(List<IMDBMovie> movies, Scene scene){
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
      s.setScene(scene);
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
      //s.setScene(askForMovies());
    });
    view.setLayout(addW,0,700);
    root.getChildren().add(addW);

    Button add = new Button("Watch");
    add.setOnAction(arg0 -> {
      model.add(movies.get(list.getSelectionModel().getSelectedIndex()));
      addRating((movies.get(list.getSelectionModel().getSelectedIndex())),  TYPES.Search);
      //s.setScene(askForMovies());
    });
    view.setLayout(add,0,650);
    root.getChildren().add(add);


    Scene sc = new Scene(root, 1000, 1000);
    s.setScene(sc);
  }
  @Override
  public void run(Stage stage) {
    this.s = stage;
    Scene sc = initScene();
    s.setScene(sc);
    s.show();
  }

  public Scene initScene() {
    this.s.setTitle("Welcome");
    b = new Button("Movies");
    b2 = new Button("Save");
    b3 = new Button("Watchlist");
    b4 = new Button("Add Movies");
    b5 = new Button("Load");
    b6 = new Button("Save as New");
    b7 = new Button("Generate Recommendation");
    setButton();
    Group g = new Group();
    g.getChildren().add(b);
    g.getChildren().add(b2);
    g.getChildren().add(b3);
    g.getChildren().add(b4);
    g.getChildren().add(b5);
    g.getChildren().add(b6);
    g.getChildren().add(b7);
    return new Scene(g, 1000, 1000);
  }

  private void realSave() throws SQLException {
    if(currModel.equalsIgnoreCase("")) {
      newSaveState();
    }
    else {
      save();
    }
  }

  @Override
  public void save() throws SQLException {
    Statement s = connection.createStatement();
    s.execute(String.format("DELETE FROM %s;", currModel));
    for (IType t : model.getMovies()){
      s.executeUpdate(String.format("INSERT INTO %s VALUES ('%s', '%s', %d, %d, %d);",
              currModel, t.getTitle(), t.getURL(), t.getYear(), t.getRating(), 1));
    }
    for (IType t : model.getWatchlist()){
      s.executeUpdate(String.format("INSERT INTO %s VALUES ('%s', '%s', %d, %d, %d);",
              currModel, t.getTitle(), t.getURL(), t.getYear(), 0, 0));
    }

  }

  @Override
  public void load(String s) throws SQLException {
    currModel = s;
    Statement st = connection.createStatement();
    ResultSet rs = st.executeQuery(String.format("SELECT * FROM %s;", currModel));
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
  }

  private void newSaveState() throws SQLException {
    Popup popup = new Popup();
    Group root = new Group();
    root.getChildren().add(new Label("Save As:"));
    TextField name = new TextField();
    root.getChildren().add(name);
    root.getChildren().add(backButton);
    backButton.setLayoutX(0);
    backButton.setLayoutY(100);
    backButton.setOnAction(arg0 -> {
      popup.hide();
    });
    Button saver = new Button("Save");
    saver.setLayoutX(50);
    saver.setLayoutY(50);
    saver.setOnAction(arg0 -> {
      Statement st;
      try {
        st = connection.createStatement();
        String save = "";
        save = name.getText();
        currModel = save;
        System.out.println(currModel);
        st.execute(String.format("CREATE TABLE %s (title varchar(64), imgURL varchar(64), year int, rating int, seen int);", currModel));
        st.executeUpdate(String.format("INSERT INTO SAVES (refs) VALUES ('%s')", currModel));
        name.clear();
        popup.hide();
        save();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    popup.getContent().add(saver);
    popup.getContent().add(root);
    popup.show(s);

  }

  private void loadScene() throws SQLException {
    Group root = new Group();
    Popup popup = new Popup();
    ListView<String> list = new ListView<>();
    ObservableList<String> items = FXCollections.observableArrayList();
    list.setItems(items);
    list.setPrefWidth(500);
    list.setPrefHeight(500);
    view.setLayout(list,0,0);
    view.setLayout(backButton,0,750);
    root.getChildren().add(backButton);
    backButton.setOnAction(arg0 -> {
      s.setScene(initScene());
    });
    root.getChildren().add(list);
    Statement s = this.connection.createStatement();
    ResultSet rs = s.executeQuery("SELECT * FROM SAVES;");
    while (rs.next()){
      items.add(rs.getString("refs"));
    }
    Button load = new Button("Load");
    view.setLayout(load, 500, 0);
    root.getChildren().add(load);
    load.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent actionEvent) {
        try {
          load(list.getSelectionModel().getSelectedItem());
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
        popup.hide();
      }
    });
    popup.getContent().add(root);
    popup.show(this.s);




  }

  public Scene infoScene(IType m){
     Group g = new Group();
     Text t = new Text(String.format("Year: %d", m.getYear()));
     t.setLayoutY(50);
     g.getChildren().add(t);
     g.getChildren().add(backButton);
     return new Scene(g, 1000, 1000);
  }

  public Scene chatGPT() throws Exception {
    if(Objects.isNull(gptRecs)){
      gptRecs = getGptRecs();
    }
    s.setTitle("Recommendations");
    Group g = new Group();
    ListView<String> list = new ListView<>();
    ObservableList<String> items = FXCollections.observableArrayList();
    list.setItems(items);
    list.setPrefWidth(500);
    list.setPrefHeight(500);
    view.setLayout(list,0,0);
    backButton.setLayoutY(0);
    backButton.setLayoutX(750);
    g.getChildren().add(backButton);
    backButton.setOnAction(arg0 -> {
      s.setScene(initScene());
    });
    g.getChildren().add(list);
    items.addAll(Arrays.asList(gptRecs));
    Button select = new Button("Select");
    select.setLayoutY(0);
    select.setLayoutX(550);
    g.getChildren().add(select);
    select.setOnAction(actionEvent -> {
      String title = list.getSelectionModel().getSelectedItem();
      g.getChildren().remove(backButton);
      try {
        displaySearchResults(IMDB.request(title.split(" ")), chatGPT());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }

    });
    return new Scene(g, 1000, 1000);
  }
  
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
