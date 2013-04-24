package quiz.strazacki.app

import com.google.gwt.core.client.EntryPoint
import com.google.gwt.event.dom.client.KeyPressEvent
import com.google.gwt.uibinder.client.UiField
import com.google.gwt.user.client.ui.Button
import com.google.gwt.user.client.ui.HTML
import com.google.gwt.user.client.ui.Image
import com.google.gwt.user.client.ui.RootPanel

import static com.google.gwt.user.client.Window.*

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
	@UiField
	protected Image prev
	@UiField
	protected HTML results
	@UiField
	protected HTML answer

	override onModuleLoad() {
		val root = RootPanel::get()
		root.add(uiBinder.createAndBindUi(this));
		buttons.forEach [ button |
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

	def buttons() {
		#[a, b, c, d, e]
	}

	var index = -1
	val data = QuizData::data.entrySet.toList.shuffle
	var good = 0
	var bad = 0

	def nextImage() {
		index = index + 1
		var no = Math::min(index + 1, data.size);
		var left = Math::max(0, data.size - index - 1);
		results.HTML = '''Pytanie: «no», Pozostało: «left»<br/>
			Dobrze: «good», Źle: «bad»<br/>'''
		if (index >= data.size) {
			buttons.forEach[enabled = false]
		} else {
			prev.url = img.url
			img.url = data.get(index).key.safeUri
		}
	}

	def processAnswer(String userAnswer) {
		val goodAnswer = data.get(index).value
		if (userAnswer == goodAnswer) {
			good = good + 1
			answer.HTML = '''<b style='color:green'>«userAnswer» - Dobrze</b>''' 
		} else {
			bad = bad + 1
			answer.HTML = '''<b style='color:red'>«userAnswer» - Źle. Przawidłowa odpowiedz: «goodAnswer»</b>''' 
		}
		nextImage
	}
	def getAnswer(KeyPressEvent e) {
		val c = e.charCode.toString.toUpperCase
		switch (c) {
			case "ABCDE".contains(c): c
			case '1': 'A'
			case '2': 'B'
			case '3': 'C'
			case '4': 'D'
			case '5': 'E'
			default: null
		}
	}
}
