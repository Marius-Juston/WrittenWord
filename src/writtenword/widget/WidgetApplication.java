package writtenword.widget;

import javafx.scene.Group;
import javafx.scene.control.Button;

/**
 * @author Marius Juston
 **/
public abstract class WidgetApplication extends Group {

	private final Button closeButton;


	public WidgetApplication() {
		closeButton = new Button("x");

		getChildren().add(closeButton);
	}

	public Button getCloseButton() {
		return closeButton;
	}

	public abstract void widgetApplication();
}
