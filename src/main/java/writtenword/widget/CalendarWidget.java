package writtenword.widget;

import com.calendarfx.view.CalendarView;

/**
 * @author Marius Juston
 **/
public class CalendarWidget extends WidgetApplication {

  public CalendarWidget() {
    CalendarView calendarView = new CalendarView();
//    calendarView.setShowSearchField(false);
//    calendarView.setShowPrintButton(false);
//    calendarView.setShowDeveloperConsole(false);
//    calendarView.setShowAddCalendarButton(false);
//    calendarView.setShowPageToolBarControls(false);
    calendarView.setShowToolBar(false);

    getChildren().add(0, calendarView);
  }
}
