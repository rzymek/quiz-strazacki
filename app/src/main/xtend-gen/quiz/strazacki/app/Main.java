package quiz.strazacki.app;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import quiz.strazacki.app.Base64;
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
  
  @UiField
  protected Image prev;
  
  @UiField
  protected HTML results;
  
  @UiField
  protected HTML answer;
  
  @UiField
  protected Anchor email;
  
  public void onModuleLoad() {
    final RootPanel root = RootPanel.get("body");
    Widget _createAndBindUi = MainUi.uiBinder.createAndBindUi(this);
    root.add(_createAndBindUi);
    List<Button> _buttons = this.buttons();
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
    IterableExtensions.<Button>forEach(_buttons, _function);
    final KeyPressHandler _function_1 = new KeyPressHandler() {
      public void onKeyPress(final KeyPressEvent it) {
        final String answer = Main.this.getAnswer(it);
        boolean _notEquals = (!Objects.equal(answer, null));
        if (_notEquals) {
          Main.this.processAnswer(answer);
        }
      }
    };
    DomEvent.Type<KeyPressHandler> _type = KeyPressEvent.getType();
    root.<KeyPressHandler>addDomHandler(_function_1, _type);
    this.nextImage();
  }
  
  @UiHandler("email")
  public void showEmail(final MouseOverEvent e) {
    String _decode = Base64.decode("ZWVyenltZWtAZ21haWwuY29tCg==");
    String _plus = ("mailto:" + _decode);
    this.email.setHref(_plus);
  }
  
  public List<Button> buttons() {
    return Collections.<Button>unmodifiableList(Lists.<Button>newArrayList(this.a, this.b, this.c, this.d, this.e));
  }
  
  private int index = (-1);
  
  private final List<Map.Entry<ImageResource, String>> data = ShuffleExtention.<Map.Entry<ImageResource, String>>shuffle(IterableExtensions.<Map.Entry<ImageResource, String>>toList(QuizData.data.entrySet()));
  
  private int good = 0;
  
  private int bad = 0;
  
  public void nextImage() {
    this.index = (this.index + 1);
    int _size = this.data.size();
    int no = Math.min((this.index + 1), _size);
    int _size_1 = this.data.size();
    int _minus = (_size_1 - this.index);
    int _minus_1 = (_minus - 1);
    int left = Math.max(0, _minus_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Pytanie: ");
    _builder.append(no, "");
    _builder.append(", Pozostało: ");
    _builder.append(left, "");
    _builder.append("<br/>");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("Dobrze: ");
    _builder.append(this.good, "\t\t\t");
    _builder.append(", Źle: ");
    _builder.append(this.bad, "\t\t\t");
    _builder.append("<br/>");
    this.results.setHTML(_builder.toString());
    int _size_2 = this.data.size();
    boolean _greaterEqualsThan = (this.index >= _size_2);
    if (_greaterEqualsThan) {
      List<Button> _buttons = this.buttons();
      final Procedure1<Button> _function = new Procedure1<Button>() {
        public void apply(final Button it) {
          it.setEnabled(false);
        }
      };
      IterableExtensions.<Button>forEach(_buttons, _function);
    } else {
      String _url = this.img.getUrl();
      this.prev.setUrl(_url);
      Map.Entry<ImageResource, String> _get = this.data.get(this.index);
      ImageResource _key = _get.getKey();
      SafeUri _safeUri = _key.getSafeUri();
      this.img.setUrl(_safeUri);
    }
  }
  
  public void processAnswer(final String userAnswer) {
    Map.Entry<ImageResource, String> _get = this.data.get(this.index);
    final String goodAnswer = _get.getValue();
    boolean _equals = Objects.equal(userAnswer, goodAnswer);
    if (_equals) {
      this.good = (this.good + 1);
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<b style=\'color:green\'>");
      _builder.append(userAnswer, "");
      _builder.append(" - Dobrze</b>");
      this.answer.setHTML(_builder.toString());
    } else {
      this.bad = (this.bad + 1);
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("<b style=\'color:red\'>");
      _builder_1.append(userAnswer, "");
      _builder_1.append(" - Źle. Prawidłowa odpowiedz: ");
      _builder_1.append(goodAnswer, "");
      _builder_1.append("</b>");
      this.answer.setHTML(_builder_1.toString());
    }
    this.nextImage();
  }
  
  public String getAnswer(final KeyPressEvent e) {
    String _xblockexpression = null;
    {
      char _charCode = e.getCharCode();
      String _string = Character.valueOf(_charCode).toString();
      final String c = _string.toUpperCase();
      String _switchResult = null;
      boolean _matched = false;
      if (!_matched) {
        boolean _contains = "ABCDE".contains(c);
        if (_contains) {
          _matched=true;
          _switchResult = c;
        }
      }
      if (!_matched) {
        if (Objects.equal(c, "1")) {
          _matched=true;
          _switchResult = "A";
        }
      }
      if (!_matched) {
        if (Objects.equal(c, "2")) {
          _matched=true;
          _switchResult = "B";
        }
      }
      if (!_matched) {
        if (Objects.equal(c, "3")) {
          _matched=true;
          _switchResult = "C";
        }
      }
      if (!_matched) {
        if (Objects.equal(c, "4")) {
          _matched=true;
          _switchResult = "D";
        }
      }
      if (!_matched) {
        if (Objects.equal(c, "5")) {
          _matched=true;
          _switchResult = "E";
        }
      }
      if (!_matched) {
        _switchResult = null;
      }
      _xblockexpression = _switchResult;
    }
    return _xblockexpression;
  }
}
