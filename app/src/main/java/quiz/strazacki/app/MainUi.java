package quiz.strazacki.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class MainUi {
	protected static MainUiBinder uiBinder = GWT.create(MainUiBinder.class);

	protected interface MainUiBinder extends UiBinder<Widget, Main> {
	}
}
