package quiz.strazacki.plugin

import com.google.common.io.Files
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.Map
import java.util.regex.Pattern
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.eclipse.xtext.xbase.lib.Pair
import com.google.common.collect.Maps

@Mojo(name="genres", defaultPhase=LifecyclePhase::GENERATE_SOURCES)
class GenRes extends AbstractMojo {
	static val regex = Pattern::compile("-([a-z])[.]")

	@Parameter(defaultValue="${basedir}/src/main/resources/img")
	public File imgDir;
	@Parameter(defaultValue="${basedir}/src/main/java/quiz/strazacki/app/Images.java")
	public File imagesJava;
	@Parameter(defaultValue="${basedir}/src/main/java/quiz/strazacki/app/QuizData.java")
	public File quizDataXtend;

	def static <T, K> Map<T, K> operator_add(Map<T, K> map, Pair<T, K> entry) {
		val result = Maps::newHashMap(map);
		result.put(entry.key, entry.value);
		return result
	}

	override execute() throws MojoExecutionException, MojoFailureException {
		try {
			val files = {
				val files = imgDir.list.sort
				if (files == null)
					throw new MojoFailureException("Can't read " + imgDir.canonicalPath)

				files.map [
					#{'filename' -> it, 'javaname' -> "i"+it.replaceAll("[.-]", "_")}
				].map [
					val m = regex.matcher(it.get('filename').toLowerCase)
					return if (m.find) {
						it += ('answer' -> m.group(1).toUpperCase)
					} else {
						null
					}
				].filter[it != null]
			}
			val images = '''
				package quiz.strazacki.app;
				
				import com.google.gwt.core.client.GWT;
				import com.google.gwt.resources.client.ClientBundle;
				import com.google.gwt.resources.client.ImageResource;
				
				public interface Images extends ClientBundle {
					Images INSTANCE = GWT.create(Images.class);
					«FOR f : files»
						@Source("«imgDir.name»/«f.get('filename')»")
						ImageResource «f.get('javaname')»();
						
					«ENDFOR»		
				}
			'''
			val quizData = '''
				package quiz.strazacki.app;
				
				import java.util.HashMap;
				import java.util.Map;
				
				import com.google.gwt.resources.client.ImageResource;
				
				public class QuizData {
					private static final Images imgs = Images.INSTANCE;
					public static Map<ImageResource, String> data = new HashMap<ImageResource, String>();
					static {
						«FOR f : files»
							data.put(imgs.«f.get('javaname')»(),"«f.get('answer')»");
						«ENDFOR»
					}
				}
			'''
			Files::write(images, imagesJava, Charset::forName("utf-8"));
			Files::write(quizData, quizDataXtend, Charset::forName("utf-8"));
		} catch (IOException e) {
			throw new MojoFailureException(e.toString, e);
		}
	}

	def static void main(String[] args) {
		val g = new GenRes
		g.imgDir = new File('../app/src/main/resources/img')
		g.execute
	}

}
