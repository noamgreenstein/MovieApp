module com.example.movieapp {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires validatorfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;
  requires eu.hansolo.tilesfx;
  //requires com.almasb.fxgl.all;
  requires java.security.jgss;
  requires java.net.http;
  requires java.sql;

  opens com.example.movieapp to javafx.fxml;
  exports com.example.movieapp;
  exports com.example.movieapp.Controller;
  opens com.example.movieapp.Controller to javafx.fxml;
}