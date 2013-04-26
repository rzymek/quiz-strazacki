package quiz.strazacki.plugin;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.Pair;

@Mojo(name = "genres", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
@SuppressWarnings("all")
public class GenRes extends AbstractMojo {
  private final static Pattern regex = new Function0<Pattern>() {
    public Pattern apply() {
      Pattern _compile = Pattern.compile("-([a-z])[.]");
      return _compile;
    }
  }.apply();
  
  @Parameter(defaultValue = "${basedir}/src/main/resources/img")
  public File imgDir;
  
  @Parameter(defaultValue = "${basedir}/src/main/java/quiz/strazacki/app/Images.java")
  public File imagesJava;
  
  @Parameter(defaultValue = "${basedir}/src/main/java/quiz/strazacki/app/QuizData.java")
  public File quizDataXtend;
  
  public static <T extends Object, K extends Object> Map<T,K> operator_add(final Map<T,K> map, final Pair<T,K> entry) {
    final HashMap<T,K> result = Maps.<T, K>newHashMap(map);
    T _key = entry.getKey();
    K _value = entry.getValue();
    result.put(_key, _value);
    return result;
  }
  
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      Iterable<Map<String,String>> _xblockexpression = null;
      {
        String[] _list = this.imgDir.list();
        final List<String> files = IterableExtensions.<String>sort(((Iterable<String>)Conversions.doWrapArray(_list)));
        boolean _equals = Objects.equal(files, null);
        if (_equals) {
          String _canonicalPath = this.imgDir.getCanonicalPath();
          String _plus = ("Can\'t read " + _canonicalPath);
          MojoFailureException _mojoFailureException = new MojoFailureException(_plus);
          throw _mojoFailureException;
        }
        final Function1<String,Map<String,String>> _function = new Function1<String,Map<String,String>>() {
            public Map<String,String> apply(final String it) {
              Map<String,String> _xsetliteral = null;
              String _replaceAll = it.replaceAll("[.-]", "_");
              String _plus = ("i" + _replaceAll);
              Builder<String,String> _builder = ImmutableMap.builder();
              _builder.put("filename", it);
              _builder.put("javaname", _plus);
              _xsetliteral = _builder.build();
              return _xsetliteral;
            }
          };
        List<Map<String,String>> _map = ListExtensions.<String, Map<String,String>>map(files, _function);
        final Function1<Map<String,String>,Map<String,String>> _function_1 = new Function1<Map<String,String>,Map<String,String>>() {
            public Map<String,String> apply(final Map<String,String> it) {
              String _get = it.get("filename");
              String _lowerCase = _get.toLowerCase();
              final Matcher m = GenRes.regex.matcher(_lowerCase);
              Map<String,String> _xifexpression = null;
              boolean _find = m.find();
              if (_find) {
                String _group = m.group(1);
                String _upperCase = _group.toUpperCase();
                Pair<String,String> _mappedTo = Pair.<String, String>of("answer", _upperCase);
                Map<String,String> _add = GenRes.<String, String>operator_add(it, _mappedTo);
                _xifexpression = _add;
              } else {
                _xifexpression = null;
              }
              return _xifexpression;
            }
          };
        List<Map<String,String>> _map_1 = ListExtensions.<Map<String,String>, Map<String,String>>map(_map, _function_1);
        final Function1<Map<String,String>,Boolean> _function_2 = new Function1<Map<String,String>,Boolean>() {
            public Boolean apply(final Map<String,String> it) {
              boolean _notEquals = (!Objects.equal(it, null));
              return Boolean.valueOf(_notEquals);
            }
          };
        Iterable<Map<String,String>> _filter = IterableExtensions.<Map<String,String>>filter(_map_1, _function_2);
        _xblockexpression = (_filter);
      }
      final Iterable<Map<String,String>> files = _xblockexpression;
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("package quiz.strazacki.app;");
      _builder.newLine();
      _builder.newLine();
      _builder.append("import com.google.gwt.core.client.GWT;");
      _builder.newLine();
      _builder.append("import com.google.gwt.resources.client.ClientBundle;");
      _builder.newLine();
      _builder.append("import com.google.gwt.resources.client.ImageResource;");
      _builder.newLine();
      _builder.newLine();
      _builder.append("public interface Images extends ClientBundle {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("Images INSTANCE = GWT.create(Images.class);");
      _builder.newLine();
      {
        for(final Map<String,String> f : files) {
          _builder.append("\t");
          _builder.append("@Source(\"");
          String _name = this.imgDir.getName();
          _builder.append(_name, "	");
          _builder.append("/");
          String _get = f.get("filename");
          _builder.append(_get, "	");
          _builder.append("\")");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("ImageResource ");
          String _get_1 = f.get("javaname");
          _builder.append(_get_1, "	");
          _builder.append("();");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.newLine();
        }
      }
      _builder.append("}");
      _builder.newLine();
      final String images = _builder.toString();
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("package quiz.strazacki.app;");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("import java.util.HashMap;");
      _builder_1.newLine();
      _builder_1.append("import java.util.Map;");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("import com.google.gwt.resources.client.ImageResource;");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("public class QuizData {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("private static final Images imgs = Images.INSTANCE;");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("public static Map<ImageResource, String> data = new HashMap<ImageResource, String>();");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("static {");
      _builder_1.newLine();
      {
        for(final Map<String,String> f_1 : files) {
          _builder_1.append("\t\t");
          _builder_1.append("data.put(imgs.");
          String _get_2 = f_1.get("javaname");
          _builder_1.append(_get_2, "		");
          _builder_1.append("(),\"");
          String _get_3 = f_1.get("answer");
          _builder_1.append(_get_3, "		");
          _builder_1.append("\");");
          _builder_1.newLineIfNotEmpty();
        }
      }
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      final String quizData = _builder_1.toString();
      Charset _forName = Charset.forName("utf-8");
      Files.write(images, this.imagesJava, _forName);
      Charset _forName_1 = Charset.forName("utf-8");
      Files.write(quizData, this.quizDataXtend, _forName_1);
    } catch (final Throwable _t) {
      if (_t instanceof IOException) {
        final IOException e = (IOException)_t;
        String _string = e.toString();
        MojoFailureException _mojoFailureException = new MojoFailureException(_string, e);
        throw _mojoFailureException;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public static void main(final String[] args) {
    try {
      GenRes _genRes = new GenRes();
      final GenRes g = _genRes;
      File _file = new File("../app/src/main/resources/img");
      g.imgDir = _file;
      g.execute();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
