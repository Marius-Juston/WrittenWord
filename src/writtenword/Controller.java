package writtenword;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Controller implements Initializable {

	public Canvas canvas;
	public ColorPicker colorChooser;
	public StackPane stackPane;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		canvas.heightProperty().bind(stackPane.heightProperty());
		canvas.widthProperty().bind(stackPane.widthProperty());

		colorChooser.setValue(Color.BLACK);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		gc.setStroke(Color.BLACK);
		gc.setLineWidth(1);

		canvas.setOnMousePressed(mouseEvent ->
			{
				gc.beginPath();
				gc.lineTo(mouseEvent.getSceneX(), mouseEvent.getSceneY());
				gc.stroke();
			}
		);

		canvas.setOnMouseDragged(mouseEvent -> {
			{
				gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
				gc.stroke();
			}
		});

		colorChooser.setOnAction(actionEvent -> gc.setStroke(colorChooser.getValue()));

	}
}
