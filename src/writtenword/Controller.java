package writtenword;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Controller implements Initializable {

	private static final double SCALE_FACTOR = .1;
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

		final double[] points = {0, 0};

		canvas.setOnMousePressed(mouseEvent ->
			{
				if (mouseEvent.getButton() == MouseButton.MIDDLE) {
					points[0] = mouseEvent.getSceneX();
					points[1] = mouseEvent.getSceneY();
				}

				gc.beginPath();
				gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
				gc.stroke();
			}
		);

		canvas.setOnMouseReleased(event -> gc.closePath());

		canvas.setOnMouseDragged(mouseEvent -> {
			{
				if (mouseEvent.getButton() == MouseButton.MIDDLE) {
					double x = mouseEvent.getSceneX() - points[0];
					double y = mouseEvent.getSceneY() - points[1];

					canvas.setTranslateX(canvas.getTranslateX() + x);
					canvas.setTranslateY(canvas.getTranslateY() + y);

					points[0] = mouseEvent.getSceneX();
					points[1] = mouseEvent.getSceneY();
				} else {
					gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
					gc.stroke();
				}
			}
		});

		canvas.setOnScroll(event -> {
			double scale = Math.max(canvas.getScaleX() + (event.getDeltaY() > 0 ? SCALE_FACTOR : -SCALE_FACTOR), .1);

			canvas.setScaleX(scale);
			canvas.setScaleY(scale);

		});

//		canvas.setOnZoom(event -> {
//			canvas.setScaleX(event.getTotalZoomFactor());
//			canvas.setScaleY(event.getTotalZoomFactor());
//		});

		colorChooser.setOnAction(actionEvent -> gc.setStroke(colorChooser.getValue()));

	}
}
