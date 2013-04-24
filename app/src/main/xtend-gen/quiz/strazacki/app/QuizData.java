package quiz.strazacki.app;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gwt.resources.client.ImageResource;
import java.util.Map;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import quiz.strazacki.app.Images;

@SuppressWarnings("all")
public class QuizData {
  private final static Images imgs = Images.INSTANCE;
  
  public final static Map<ImageResource,String> data = new Function0<Map<ImageResource,String>>() {
    public Map<ImageResource,String> apply() {
      Map<ImageResource,String> _xsetliteral = null;
      ImageResource _img01 = QuizData.imgs.img01();
      ImageResource _img02 = QuizData.imgs.img02();
      Builder<ImageResource,String> _builder = ImmutableMap.builder();
      _builder.put(_img01, "D");
      _builder.put(_img02, "A");
      _xsetliteral = _builder.build();
      return _xsetliteral;
    }
  }.apply();
}
