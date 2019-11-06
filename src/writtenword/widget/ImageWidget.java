package writtenword.widget;

import javafx.scene.image.ImageView;

/**
 * @author Marius Juston
 **/
public class ImageWidget extends WidgetApplication {

	public ImageWidget(String url) {
		ImageView imageView = new ImageView(url);
		imageView.setPreserveRatio(true);
		imageView.setFitHeight(200.0);

		getChildren().add(imageView);
	}

	@Override
	public void widgetApplication() {

	}
}
