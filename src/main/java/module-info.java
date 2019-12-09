module writtenword.main {
  requires javafx.controls;
  requires javafx.fxml;
  requires google.api.client;
  requires google.oauth.client.jetty;
  requires google.oauth.client.java6;
  requires google.oauth.client;
  requires com.calendarfx.view;
  requires javafx.swing;

  opens writtenword;
  opens writtenword.google;
  opens writtenword.widget;
}