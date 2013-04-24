package quiz.strazacki.plugin;

import com.google.common.base.Objects;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.InputOutput;

@Mojo(name = "genres")
@SuppressWarnings("all")
public class GenRes extends AbstractMojo {
  @Parameter(required = true)
  public File imgDir;
  
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      final String[] files = this.imgDir.list();
      boolean _equals = Objects.equal(files, null);
      if (_equals) {
        String _canonicalPath = this.imgDir.getCanonicalPath();
        String _plus = ("Can\'t read " + _canonicalPath);
        MojoFailureException _mojoFailureException = new MojoFailureException(_plus);
        throw _mojoFailureException;
      }
      int i = 1;
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
        for(final String f : files) {
          _builder.append("\t");
          _builder.append("@Source(\"");
          String _name = this.imgDir.getName();
          _builder.append(_name, "	");
          _builder.append("/");
          _builder.append(f, "	");
          _builder.append("\")");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("ImageResource img");
          int _plus_1 = (i + 1);
          int _i = i = _plus_1;
          _builder.append(_i, "	");
          _builder.append("();");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.newLine();
        }
      }
      _builder.append("}");
      _builder.newLine();
      final String images = _builder.toString();
      i = 1;
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append("package quiz.strazacki.app");
      _builder_1.newLine();
      _builder_1.newLine();
      _builder_1.append("class QuizData {");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("static val imgs = Images::INSTANCE");
      _builder_1.newLine();
      _builder_1.append("\t");
      _builder_1.append("public static val data = #{");
      _builder_1.newLine();
      {
        for(final String f_1 : files) {
          _builder_1.append("\t\t");
          _builder_1.append("imgs.img");
          int _plus_2 = (i + 1);
          int _i_1 = i = _plus_2;
          _builder_1.append(_i_1, "		");
          _builder_1.append(" -> \'");
          String _answerFromName = this.getAnswerFromName(f_1);
          _builder_1.append(_answerFromName, "		");
          _builder_1.append("\'");
          _builder_1.newLineIfNotEmpty();
        }
      }
      _builder_1.append("\t");
      _builder_1.append("}");
      _builder_1.newLine();
      _builder_1.append("}");
      _builder_1.newLine();
      final String quizData = _builder_1.toString();
      InputOutput.<String>println(quizData);
    } catch (final Throwable _t) {
      if (_t instanceof IOException) {
        final IOException e = (IOException)_t;
        String _string = e.toString();
        MojoFailureException _mojoFailureException_1 = new MojoFailureException(_string, e);
        throw _mojoFailureException_1;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  private final Pattern regex = new Function0<Pattern>() {
    public Pattern apply() {
      Pattern _compile = Pattern.compile("-([a-z])[.]");
      return _compile;
    }
  }.apply();
  
  public String getAnswerFromName(final String name) {
    try {
      String _xblockexpression = null;
      {
        String _lowerCase = name.toLowerCase();
        final Matcher m = this.regex.matcher(_lowerCase);
        boolean _find = m.find();
        boolean _not = (!_find);
        if (_not) {
          String _plus = ("No answer in filename: " + name);
          MojoFailureException _mojoFailureException = new MojoFailureException(_plus);
          throw _mojoFailureException;
        }
        String _group = m.group(1);
        String _upperCase = _group.toUpperCase();
        _xblockexpression = (_upperCase);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
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
