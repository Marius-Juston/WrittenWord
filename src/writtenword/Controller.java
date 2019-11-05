package writtenword;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Scale;

public class Controller implements Initializable {

	private static final double SCALE_FACTOR = .1;
	public Pane canvas;
	public ColorPicker colorChooser;
	public StackPane stackPane;
	public MenuButton widgetMenu;

	private ArrayList<Path> paths = new ArrayList<>();

	private Point2D getActualPoint(MouseEvent mouseEvent) {
		try {
			return canvas.getLocalToParentTransform().inverseTransform(mouseEvent.getX(), mouseEvent.getY());
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}

		return Point2D.ZERO;
	}

	private void initWidgets() {
		widgetMenu.getItems().add(new Widget());
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initWidgets();

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

				Point2D actualPoint = getActualPoint(mouseEvent);
				path.getElements()
					.add(new MoveTo(actualPoint.getX(), actualPoint.getY()));
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

					Point2D point2D = getActualPoint(mouseEvent);
					paths.get(paths.size() - 1).getElements().add(new LineTo(point2D.getX(), point2D.getY()));
				}
			}
		});

		stackPane.setOnScroll(event -> {
			double scale = 1.0 - (event.getDeltaY() > 0 ? SCALE_FACTOR : -SCALE_FACTOR);

			if (event.getDeltaY() < 0 || canvas.getLocalToParentTransform().getMxx() > .1) {
				try {
					Point2D point2D = canvas.getLocalToParentTransform().inverseTransform(event.getX(), event.getY());
					canvas.getTransforms().add(new Scale(scale, scale, point2D.getX(), point2D.getY()));
				} catch (NonInvertibleTransformException e) {
					e.printStackTrace();
				}
			}

		});

//		canvas.setOnZoom(event -> {
//			canvas.setScaleX(event.getTotalZoomFactor());
//			canvas.setScaleY(event.getTotalZoomFactor());
//		});

		colorChooser.setOnAction(actionEvent -> paths.get(paths.size() - 1).setStroke(colorChooser.getValue()));
	}
}
