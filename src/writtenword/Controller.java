package writtenword;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Controller implements Initializable {

	private static final double SCALE_FACTOR = .1;
	public Pane canvas;
	public ColorPicker colorChooser;
	public StackPane stackPane;

	private ArrayList<Path> paths = new ArrayList<>();

	private double getActualX(MouseEvent mouseEvent) {
		return (mouseEvent.getSceneX() - canvas.getTranslateX() - canvas.getWidth() / 2.0) / canvas.getScaleX()
			+ canvas.getWidth() / 2.0;
	}


	private double getActualY(MouseEvent mouseEvent) {
		return (mouseEvent.getSceneY() - canvas.getTranslateY() - canvas.getHeight() / 2.0) / canvas.getScaleY()
			+ canvas.getHeight() / 2.0;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		colorChooser.setValue(Color.BLACK);
//		canvas.setStyle("-fx-border-color: red");

		final double[] points = {0, 0};

		stackPane.setOnMousePressed(mouseEvent ->
			{
				if (mouseEvent.getButton() == MouseButton.MIDDLE) {
					points[0] = mouseEvent.getSceneX();
					points[1] = mouseEvent.getSceneY();
				}

				Path path = new Path();
				path.setStroke(colorChooser.getValue());
				path.getElements()
					.add(new MoveTo(getActualX(mouseEvent), getActualY(mouseEvent)));
				canvas.getChildren().add(path);
				paths.add(path);
			}
		);

//		stackPane.setOnMouseReleased(event -> paths.get(paths.size() - 1).getElements().add(new ClosePath()));

		stackPane.setOnMouseDragged(mouseEvent -> {
			{
				if (mouseEvent.getButton() == MouseButton.MIDDLE) {
					double x = mouseEvent.getSceneX() - points[0];
					double y = mouseEvent.getSceneY() - points[1];

					canvas.setTranslateX(canvas.getTranslateX() + x);
					canvas.setTranslateY(canvas.getTranslateY() + y);

					points[0] = mouseEvent.getSceneX();
					points[1] = mouseEvent.getSceneY();
				} else {

					paths.get(paths.size() - 1).getElements()
						.add(new LineTo(getActualX(mouseEvent), getActualY(mouseEvent)));
				}
			}
		});

		stackPane.setOnScroll(event -> {
			double scale = Math.max(canvas.getScaleX() + (event.getDeltaY() > 0 ? SCALE_FACTOR : -SCALE_FACTOR), .1);

			canvas.setScaleX(scale);
			canvas.setScaleY(scale);
		});

//		canvas.setOnZoom(event -> {
//			canvas.setScaleX(event.getTotalZoomFactor());
//			canvas.setScaleY(event.getTotalZoomFactor());
//		});

		colorChooser.setOnAction(actionEvent -> paths.get(paths.size() - 1).setStroke(colorChooser.getValue()));
	}
}
