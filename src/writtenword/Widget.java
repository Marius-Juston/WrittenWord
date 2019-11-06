package writtenword;

import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

/**
 * @author Marius Juston
 **/
public class Widget extends MenuItem {

	public Widget(String name, String widgetPreviewImageUrl) {
		super(name);

		ImageView imageView = new ImageView(widgetPreviewImageUrl);
//		imageView.setFitHeight(300);
		imageView.setFitWidth(100);
		imageView.setPreserveRatio(true);
		setGraphic(imageView);
	}
}
