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
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
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
    Type<KeyPressHandler> _type = KeyPressEvent.getType();
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
    List<Button> _xlistliteral = null;
    Builder<Button> _builder = ImmutableList.builder();
    _builder.add(this.a);
    _builder.add(this.b);
    _builder.add(this.c);
    _builder.add(this.d);
    _builder.add(this.e);
    _xlistliteral = _builder.build();
    return _xlistliteral;
  }
  
  private int index = new Function0<Integer>() {
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
  
  private int good = 0;
  
  private int bad = 0;
  
  public void nextImage() {
    int _plus = (this.index + 1);
    this.index = _plus;
    int _plus_1 = (this.index + 1);
    int _size = this.data.size();
    int no = Math.min(_plus_1, _size);
    int _size_1 = this.data.size();
    int _minus = (_size_1 - this.index);
    int _minus_1 = (_minus - 1);
    int left = Math.max(0, _minus_1);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Pytanie: ");
    _builder.append(no, "");
    _builder.append(", Pozosta\u0142o: ");
    _builder.append(left, "");
    _builder.append("<br/>");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("Dobrze: ");
    _builder.append(this.good, "			");
    _builder.append(", \u0179le: ");
    _builder.append(this.bad, "			");
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
      Entry<ImageResource,String> _get = this.data.get(this.index);
      ImageResource _key = _get.getKey();
      SafeUri _safeUri = _key.getSafeUri();
      this.img.setUrl(_safeUri);
    }
  }
  
  public void processAnswer(final String userAnswer) {
    Entry<ImageResource,String> _get = this.data.get(this.index);
    final String goodAnswer = _get.getValue();
    boolean _equals = Objects.equal(userAnswer, goodAnswer);
    if (_equals) {
      int _plus = (this.good + 1);
      this.good = _plus;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<b style=\'color:green\'>");
      _builder.append(userAnswer, "");
      _builder.append(" - Dobrze</b>");
      this.answer.setHTML(_builder.toString());
    } else {
      int _plus_1 = (this.bad + 1);
      this.bad = _plus_1;
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("<b style=\'color:red\'>");
      _builder_1.append(userAnswer, "");
      _builder_1.append(" - \u0179le. Prawid\u0142owa odpowiedz: ");
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
        if (Objects.equal(c,"1")) {
          _matched=true;
          _switchResult = "A";
        }
      }
      if (!_matched) {
        if (Objects.equal(c,"2")) {
          _matched=true;
          _switchResult = "B";
        }
      }
      if (!_matched) {
        if (Objects.equal(c,"3")) {
          _matched=true;
          _switchResult = "C";
        }
      }
      if (!_matched) {
        if (Objects.equal(c,"4")) {
          _matched=true;
          _switchResult = "D";
        }
      }
      if (!_matched) {
        if (Objects.equal(c,"5")) {
          _matched=true;
          _switchResult = "E";
        }
      }
      if (!_matched) {
        _switchResult = null;
      }
      _xblockexpression = (_switchResult);
    }
    return _xblockexpression;
  }
}
