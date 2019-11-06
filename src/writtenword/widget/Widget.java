package writtenword.widget;

import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * @author Marius Juston
 **/
public class Widget extends MenuItem {

	private static final double WIDTH = 100.0;
	private final WidgetApplication widgetApplication;

	public Widget(String name, String widgetPreviewImageUrl, WidgetApplication widgetApplication) {
		super(name);
		this.widgetApplication = widgetApplication;

		ImageView imageView = new ImageView(widgetPreviewImageUrl);
		imageView.setFitWidth(WIDTH);
		imageView.setPreserveRatio(true);
		setGraphic(imageView);

	}

	public WidgetApplication getWidgetApplication() {
		return widgetApplication;
	}

	public void setupWidget(Pane masterNode) {
		masterNode.getChildren().add(0, getWidgetApplication());

		getWidgetApplication().getCloseButton()
			.setOnAction(event -> masterNode.getChildren().remove(getWidgetApplication()));
	}
}
