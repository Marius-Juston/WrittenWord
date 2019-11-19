package writtenword;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		URL resource = getClass().getResource("/written_word.fxml");
		Parent root = FXMLLoader.load(resource);
		primaryStage.setTitle("Written Word");
		primaryStage.setScene(new Scene(root, 320, 240));
//		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setFullScreen(true);
		primaryStage.show();
	}
}
