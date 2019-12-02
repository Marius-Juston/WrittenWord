package writtenword.widget;

import javafx.scene.Group;
import javafx.scene.control.Button;

/**
 * @author Marius Juston
 **/
public class WidgetApplication extends Group {

  private final Button closeButton;


  public WidgetApplication() {
    closeButton = new Button("x");

    getChildren().add(closeButton);
  }

  public Button getCloseButton() {
    return closeButton;
  }
}
