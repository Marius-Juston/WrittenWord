package writtenword;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Scale;
import writtenword.widget.ImageWidget;
import writtenword.widget.Widget;

public class Controller implements Initializable {

	private static final double SCALE_FACTOR = .1;
	public Pane canvas;
	public ColorPicker colorChooser;
	public StackPane stackPane;
	public MenuButton widgetMenu;
	public Pane widgetMover;
	public HBox widgetGroup;

	private ArrayList<Path> paths = new ArrayList<>();

	private Point2D getActualPoint(MouseEvent mouseEvent) {
		try {
			return canvas.getLocalToParentTransform().inverseTransform(mouseEvent.getSceneX(), mouseEvent.getSceneY());
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}

		return Point2D.ZERO;
	}

	private void initWidgets() {
		String googleCalendar = "https://collegeinfogeek.com/wp-content/uploads/2016/08/Google_Calendar_Logo.png";
		String googleDrive = "https://assets.ifttt.com/images/channels/142226432/icons/on_color_large.png";

		widgetMenu.getItems().add(new Widget("Google Calendar", googleCalendar, new ImageWidget(googleCalendar)));
		widgetMenu.getItems().add(new Widget("Google Drive", googleDrive, new ImageWidget(googleDrive)));

		for (MenuItem item : widgetMenu.getItems()) {
			item.setOnAction(event -> ((Widget) item).setupWidget(canvas));
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initWidgets();

		widgetMover.maxHeightProperty().bind(widgetMenu.heightProperty());
		widgetGroup.maxHeightProperty().bind(widgetMenu.heightProperty());
		double width = 19.0 + 85.0 + 2.0 / 3.0;

		widgetGroup.setPrefWidth(width);
		widgetGroup.setMaxWidth(width);
//		widgetGroup.maxWidthProperty().bind(widgetMenu.prefWidthProperty().add(widgetMover.prefWidthProperty()));
		widgetGroup.setStyle("-fx-border-color: green");

		final double[] widgetsPoint = {0, 0};

		widgetMover.setOnMousePressed(event -> {
			widgetsPoint[0] = event.getX();
			widgetsPoint[1] = event.getY();
		});

		widgetMover.setOnMouseDragged(event -> {
			double x = event.getX() - widgetsPoint[0] + widgetGroup.getTranslateX();
			double y = event.getY() - widgetsPoint[1] + widgetGroup.getTranslateY();

			widgetGroup.setTranslateX(x);
			widgetGroup.setTranslateY(y);
		});

		colorChooser.setValue(Color.BLACK);

		final double[] canvasPoints = {0, 0};

		stackPane.setOnMousePressed(mouseEvent ->
			{
				if (mouseEvent.getTarget().equals(stackPane) || mouseEvent.getTarget().equals(canvas)) {

					if (mouseEvent.getButton() == MouseButton.MIDDLE) {
						canvasPoints[0] = mouseEvent.getSceneX();
						canvasPoints[1] = mouseEvent.getSceneY();
					}

					Path path = new Path();
					path.setStroke(colorChooser.getValue());

					Point2D actualPoint = getActualPoint(mouseEvent);
					path.getElements()
						.add(new MoveTo(actualPoint.getX(), actualPoint.getY()));
					canvas.getChildren().add(path);
					paths.add(path);
				}
			}
		);

//		stackPane.setOnMouseReleased(event -> paths.get(paths.size() - 1).getElements().add(new ClosePath()));

		stackPane.setOnMouseDragged(mouseEvent -> {
			{
				if (mouseEvent.getTarget().equals(stackPane) || mouseEvent.getTarget().equals(canvas)) {
					if (mouseEvent.getButton() == MouseButton.MIDDLE) {
						double x = mouseEvent.getSceneX() - canvasPoints[0];
						double y = mouseEvent.getSceneY() - canvasPoints[1];

						canvas.setTranslateX(canvas.getTranslateX() + x);
						canvas.setTranslateY(canvas.getTranslateY() + y);

						canvasPoints[0] = mouseEvent.getSceneX();
						canvasPoints[1] = mouseEvent.getSceneY();
					} else {

						Point2D point2D = getActualPoint(mouseEvent);
						paths.get(paths.size() - 1).getElements().add(new LineTo(point2D.getX(), point2D.getY()));
					}
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