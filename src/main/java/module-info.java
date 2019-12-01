module writtenword.main {
	requires javafx.controls;
	requires javafx.fxml;
	requires google.api.client;
	requires google.api.services.calendar.v3.rev305;
	requires google.http.client;
	requires google.http.client.jackson2;
	requires google.oauth.client.jetty;
	requires google.oauth.client.java6;
	requires google.oauth.client;

	opens writtenword;
	opens writtenword.google;
	opens writtenword.widget;
}