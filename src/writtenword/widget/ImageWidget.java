package writtenword.widget;

import javafx.scene.image.ImageView;

/**
 * @author Marius Juston
 **/
public class ImageWidget extends WidgetApplication {

	public ImageWidget(String url) {
		getChildren().add(new ImageView(url));
	}

	@Override
	public void widgetApplication() {

	}
}
