package quiz.strazacki.app;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import quiz.strazacki.app.MainUi;
import quiz.strazacki.app.QuizData;
import quiz.strazacki.app.ShuffleExtention;

@SuppressWarnings("all")
public class Main extends MainUi implements EntryPoint {
  @UiField
  protected Button a;
  
  @UiField
  protected Button b;
  
  @UiField
  protected Button c;
  
  @UiField
  protected Button d;
  
  @UiField
  protected Button e;
  
  @UiField
  protected Image img;
  
  public void onModuleLoad() {
    final RootPanel root = RootPanel.get();
    Widget _createAndBindUi = MainUi.uiBinder.createAndBindUi(this);
    root.add(_createAndBindUi);
    List<Button> _xlistliteral = null;
    Builder<Button> _builder = ImmutableList.builder();
    _builder.add(this.a);
    _builder.add(this.b);
    _builder.add(this.c);
    _builder.add(this.d);
    _builder.add(this.e);
    _xlistliteral = _builder.build();
    final Procedure1<Button> _function = new Procedure1<Button>() {
        public void apply(final Button button) {
          final ClickHandler _function = new ClickHandler() {
              public void onClick(final ClickEvent it) {
                String _text = button.getText();
                Main.this.processAnswer(_text);
              }
            };
          button.addClickHandler(_function);
        }
      };
    IterableExtensions.<Button>forEach(_xlistliteral, _function);
    final KeyPressHandler _function_1 = new KeyPressHandler() {
        public void onKeyPress(final KeyPressEvent it) {
          final String answer = Main.this.getAnswer(it);
          boolean _notEquals = (!Objects.equal(answer, null));
          if (_notEquals) {
            Main.this.processAnswer(answer);
          }
        }
      };
    Type<KeyPressHandler> _type = KeyPressEvent.getType();
    root.<KeyPressHandler>addDomHandler(_function_1, _type);
    this.nextImage();
  }
  
  private int currentQuestion = new Function0<Integer>() {
    public Integer apply() {
      int _minus = (-1);
      return _minus;
    }
  }.apply();
  
  private final List<Entry<ImageResource,String>> data = new Function0<List<Entry<ImageResource,String>>>() {
    public List<Entry<ImageResource,String>> apply() {
      Set<Entry<ImageResource,String>> _entrySet = QuizData.data.entrySet();
      List<Entry<ImageResource,String>> _list = IterableExtensions.<Entry<ImageResource,String>>toList(_entrySet);
      List<Entry<ImageResource,String>> _shuffle = ShuffleExtention.<Entry<ImageResource,String>>shuffle(_list);
      return _shuffle;
    }
  }.apply();
  
  public void nextImage() {
    int _plus = (this.currentQuestion + 1);
    int _size = this.data.size();
    int _modulo = (_plus % _size);
    this.currentQuestion = _modulo;
    Entry<ImageResource,String> _get = this.data.get(this.currentQuestion);
    ImageResource _key = _get.getKey();
    SafeUri _safeUri = _key.getSafeUri();
    this.img.setUrl(_safeUri);
  }
  
  public void processAnswer(final String userAnswer) {
    Entry<ImageResource,String> _get = this.data.get(this.currentQuestion);
    final String goodAnswer = _get.getValue();
    boolean _equals = Objects.equal(userAnswer, goodAnswer);
    if (_equals) {
      Window.alert("Dobrze!");
    } else {
      String _plus = (userAnswer + " - \u0179le. Przawid\u0142owa odpowiedz: ");
      String _plus_1 = (_plus + goodAnswer);
      Window.alert(_plus_1);
    }
    this.nextImage();
  }
  
  public String getAnswer(final KeyPressEvent e) {
    String _switchResult = null;
    char _charCode = e.getCharCode();
    String _string = Character.valueOf(_charCode).toString();
    final String _switchValue = _string;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(_switchValue,"1")) {
        _matched=true;
        _switchResult = "A";
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"2")) {
        _matched=true;
        _switchResult = "B";
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"3")) {
        _matched=true;
        _switchResult = "C";
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"4")) {
        _matched=true;
        _switchResult = "D";
      }
    }
    if (!_matched) {
      if (Objects.equal(_switchValue,"5")) {
        _matched=true;
        _switchResult = "E";
      }
    }
    if (!_matched) {
      _switchResult = null;
    }
    return _switchResult;
  }
}
