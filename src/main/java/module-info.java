module writtenword.main {
  requires javafx.controls;
  requires javafx.fxml;
  requires google.api.client;
  requires google.http.client.jackson2;
  requires google.oauth.client.jetty;
  requires google.oauth.client.java6;
  requires google.oauth.client;
  requires com.calendarfx.view;
  requires javafx.swing;
  requires com.google.api.client;
  requires google.api.services.calendar.v3.rev401;
  requires com.google.common;
  requires google.photos.library.client;
  requires gax;
  requires google.auth.library.credentials;
  requires google.auth.library.oauth2.http;

  opens writtenword;
  opens writtenword.google;
  opens writtenword.widget;
}