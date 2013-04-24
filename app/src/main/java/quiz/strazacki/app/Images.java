package quiz.strazacki.app;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle {
	Images INSTANCE = GWT.create(Images.class);
	
	@Source("img/01-d.png")
	ImageResource img01();

	@Source("img/02-a.png")
	ImageResource img02();

	@Source("img/03-c.png")
	ImageResource img03();
}
