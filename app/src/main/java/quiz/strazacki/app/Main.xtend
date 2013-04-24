package quiz.strazacki.app

import com.google.gwt.core.client.EntryPoint
import com.google.gwt.event.dom.client.KeyPressEvent
import com.google.gwt.uibinder.client.UiField
import com.google.gwt.user.client.Window
import com.google.gwt.user.client.ui.Button
import com.google.gwt.user.client.ui.Image
import com.google.gwt.user.client.ui.RootPanel

import static extension quiz.strazacki.app.ShuffleExtention.*

class Main extends MainUi implements EntryPoint {
	@UiField
	protected Button a
	@UiField
	protected Button b
	@UiField
	protected Button c
	@UiField
	protected Button d
	@UiField
	protected Button e
	@UiField
	protected Image img

	override onModuleLoad() {
		val root = RootPanel::get()
		root.add(uiBinder.createAndBindUi(this));
		#[a, b, c, d, e].forEach [ button |
			button.addClickHandler [
				processAnswer(button.text)
			]
		]
		root.addDomHandler(
			[
				val answer = getAnswer(it)
				if (answer != null)
					processAnswer(answer)
			], KeyPressEvent::getType());
		nextImage;
	}

	var currentQuestion = -1
	val data = QuizData::data.entrySet.toList.shuffle

	def nextImage() {
		currentQuestion = (currentQuestion + 1) % data.size
		img.url = data.get(currentQuestion).key.safeUri
	}

	def processAnswer(String userAnswer) {
		val goodAnswer = data.get(currentQuestion).value
		if (userAnswer == goodAnswer) {
			Window::alert("Dobrze!")
		} else {
			Window::alert(userAnswer+" - Źle. Przawidłowa odpowiedz: " + goodAnswer)
		}
		nextImage
	}

	def getAnswer(KeyPressEvent e) {
		switch (e.charCode.toString) {
			case '1': 'A'
			case '2': 'B'
			case '3': 'C'
			case '4': 'D'
			case '5': 'E'
			default: null
		}
	}
}
