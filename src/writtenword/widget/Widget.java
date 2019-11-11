package writtenword.widget;

import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * @author Marius Juston
 **/
public class Widget extends MenuItem {

	private static final double WIDTH = 100.0;
	private final WidgetType widgetType;
	private final Object[] args;

	public Widget(String name, String widgetPreviewImageUrl, WidgetType widgetType, Object... args) {
		super(name);
		this.widgetType = widgetType;
		this.args = args;

		ImageView imageView = new ImageView(widgetPreviewImageUrl);
		imageView.setFitWidth(WIDTH);
		imageView.setPreserveRatio(true);
		setGraphic(imageView);
	}

	public WidgetApplication getWidget() {
		switch (widgetType) {
			case IMAGE:
				return new ImageWidget((String) args[0]);
			default:
				return new WidgetApplication();
		}
	}

	public void setupWidget(Pane masterNode) {
		WidgetApplication widgetApplication = getWidget();

		masterNode.getChildren().add(widgetApplication);

		EventHandler<? super MouseEvent> setOnMouseMoved = masterNode.getOnMouseMoved();

		masterNode.setOnMouseMoved(event -> {
			widgetApplication.setTranslateX(event.getX());
			widgetApplication.setTranslateY(event.getY());
		});

		widgetApplication.getCloseButton().setOnMouseClicked(event -> {

			masterNode.setOnMouseMoved(setOnMouseMoved);
			widgetApplication.getCloseButton().setOnMouseClicked(null);

			widgetApplication.getCloseButton()
				.setOnAction(event1 -> masterNode.getChildren().remove(widgetApplication));
		});
	}
}
