package writtenword;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventType;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Scale;
import writtenword.widget.Widget;
import writtenword.widget.WidgetType;

public class Controller implements Initializable {

	public static final double MIN_SCALE = .1;
	public static final double MAX_SCALE = 10;
	private static final double SCALE_FACTOR = .1;
	private static final long PAN_CONSTANT = 450;
	private static final long ZOOM_CONSTANT = 1000;
	public Pane canvas;
	public ColorPicker colorChooser;
	public StackPane stackPane;
	public MenuButton widgetMenu;
	public Pane widgetMover;
	public HBox widgetGroup;
	public Group paths;

	private Point2D getActualPoint(double x, double y) {
		try {
			return canvas.getLocalToParentTransform().inverseTransform(x, y);
		} catch (NonInvertibleTransformException e) {
			e.printStackTrace();
		}

		return Point2D.ZERO;
	}

	private Point2D getActualPoint(MouseEvent mouseEvent) {
		return getActualPoint(mouseEvent.getSceneX(), mouseEvent.getSceneY());
	}

	private Point2D getActualPoint(ScrollEvent scrollEvent) {
		return getActualPoint(scrollEvent.getX(), scrollEvent.getY());
	}

	private Point2D getActualPoint(TouchPoint touchPoint) {
		return getActualPoint(touchPoint.getSceneX(), touchPoint.getSceneY());
	}

	private void initWidgets() {
		String googleCalendar = "https://collegeinfogeek.com/wp-content/uploads/2016/08/Google_Calendar_Logo.png";
		String googleDrive = "https://assets.ifttt.com/images/channels/142226432/icons/on_color_large.png";

		widgetMenu.getItems().add(new Widget("Google Calendar", googleCalendar, WidgetType.IMAGE, googleCalendar));
		widgetMenu.getItems().add(new Widget("Google Drive", googleDrive, WidgetType.IMAGE, googleDrive));

		widgetMenu.getItems()
			.forEach(menuItem -> menuItem.setOnAction(event -> ((Widget) menuItem).setupWidget(canvas)));

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
	}

	public void initMouseInterface() {
		final double[] canvasPoints = {0, 0};

		stackPane.setOnMousePressed(mouseEvent ->
			{
				if (mouseEvent.getTarget().equals(stackPane) || mouseEvent.getTarget().equals(canvas)) {

					if (mouseEvent.getButton() == MouseButton.MIDDLE) {
						canvasPoints[0] = mouseEvent.getSceneX();
						canvasPoints[1] = mouseEvent.getSceneY();
					} else {
						Path path = new Path();
						path.setStroke(colorChooser.getValue());

						Point2D actualPoint = getActualPoint(mouseEvent);
						path.getElements()
							.add(new MoveTo(actualPoint.getX(), actualPoint.getY()));
						canvas.getChildren().add(path);
						paths.getChildren().add(path);
					}

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
						((Path) paths.getChildren().get(paths.getChildren().size() - 1)).getElements()
							.add(new LineTo(point2D.getX(), point2D.getY()));
					}
				}
			}
		});

		stackPane.setOnScroll(event -> {
			double scale = 1.0 - (event.getDeltaY() > 0 ? SCALE_FACTOR : -SCALE_FACTOR);

			if ((event.getDeltaY() < 0 || canvas.getLocalToParentTransform().getMxx() > MIN_SCALE) && (
				event.getDeltaY() > 0 || canvas.getLocalToParentTransform().getMxx() < MAX_SCALE)) {
				Point2D point2D = getActualPoint(event);
				canvas.getTransforms().add(new Scale(scale, scale, point2D.getX(), point2D.getY()));
			}
		});
	}

	public void initTouchInterface() {
		final MovementType[] currentMovementType = {MovementType.DRAW};

		final long[] touchStartTime = {0};
		final EventType[] previousEventType = new EventType[1];

		stackPane.addEventHandler(TouchEvent.ANY, event -> {
			if (event.getEventType().equals(TouchEvent.TOUCH_PRESSED)) {
				touchStartTime[0] = System.currentTimeMillis();
			} else if (event.getEventType().equals(TouchEvent.TOUCH_RELEASED)) {
				currentMovementType[0] = MovementType.DRAW;
				System.out.println(System.currentTimeMillis() - touchStartTime[0]);
			} else {
				if (previousEventType[0] == TouchEvent.TOUCH_STATIONARY) {
					long deltaTime = System.currentTimeMillis() - touchStartTime[0];

					if (deltaTime > ZOOM_CONSTANT) {
						currentMovementType[0] = MovementType.ZOOM;
					} else if (deltaTime > PAN_CONSTANT) {
						currentMovementType[0] = MovementType.PAN;
					}
				}
			}

			previousEventType[0] = event.getEventType();
		});

		final double[] touchPoints = {0, 0};

		stackPane.setOnTouchPressed(touchEvent -> {
			if (touchEvent.getTarget().equals(stackPane) || touchEvent.getTarget().equals(canvas)) {
				TouchPoint touchPoint1 = touchEvent.getTouchPoint();

				touchPoints[0] = touchPoint1.getSceneX();
				touchPoints[1] = touchPoint1.getSceneY();

				Path path = new Path();
				path.setStroke(colorChooser.getValue());

				Point2D actualPoint = getActualPoint(touchPoint1);
				path.getElements().add(new MoveTo(actualPoint.getX(), actualPoint.getY()));
				canvas.getChildren().add(path);
				paths.getChildren().add(path);
			}
		});

		stackPane.setOnTouchStationary(touchEvent -> {
			if (touchEvent.getTarget().equals(stackPane) || touchEvent.getTarget().equals(canvas)) {
				TouchPoint touchPoint1 = touchEvent.getTouchPoint();

				touchPoints[0] = touchPoint1.getSceneX();
				touchPoints[1] = touchPoint1.getSceneY();
			}
		});

		stackPane.setOnTouchMoved(event -> {
			if (event.getTarget().equals(stackPane) || event.getTarget().equals(canvas)) {
				final TouchPoint touchPoint = event.getTouchPoint();
				if (currentMovementType[0] == MovementType.DRAW) {
					Point2D point2D = getActualPoint(touchPoint);
					if (paths.getChildren().size() > 0) {
						((Path) paths.getChildren().get(paths.getChildren().size() - 1)).getElements()
							.add(new LineTo(point2D.getX(), point2D.getY()));
					}
				} else if (currentMovementType[0] == MovementType.PAN) {
					double x = touchPoint.getSceneX() - touchPoints[0];
					double y = touchPoint.getSceneY() - touchPoints[1];

					canvas.setTranslateX(canvas.getTranslateX() + x);
					canvas.setTranslateY(canvas.getTranslateY() + y);

					touchPoints[0] = touchPoint.getSceneX();
					touchPoints[1] = touchPoint.getSceneY();
				} else if (currentMovementType[0] == MovementType.ZOOM) {
					double y = touchPoint.getSceneY() - touchPoints[1];

						final double scale = 1 - SCALE_FACTOR * Math.signum(y);
					if ((y > 0 || canvas.getLocalToParentTransform().getMxx() > MIN_SCALE) && (
						y < 0 || canvas.getLocalToParentTransform().getMxx() < MAX_SCALE)) {

						canvas.getTransforms().add(new Scale(scale, scale, touchPoints[0], touchPoints[1]));
					}
				}
			}
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initWidgets();
//		initMouseInterface();
		initTouchInterface();

		colorChooser.setValue(Color.BLACK);

//		canvas.setOnZoom(event -> {
//			canvas.setScaleX(event.getTotalZoomFactor());
//			canvas.setScaleY(event.getTotalZoomFactor());
//		});

		colorChooser.setOnAction(actionEvent -> ((Path) paths.getChildren().get(paths.getChildren().size() - 1))
			.setStroke(colorChooser.getValue()));
	}

	enum MovementType {
		PAN, ZOOM, DRAW
	}
}
