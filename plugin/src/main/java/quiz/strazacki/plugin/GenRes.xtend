package quiz.strazacki.plugin

import java.io.File
import java.io.IOException
import java.util.regex.Pattern
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException
import org.apache.maven.plugin.MojoFailureException
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter

@Mojo(name="genres")
class GenRes extends AbstractMojo {
	@Parameter(required=true)
	public File imgDir;

	override execute() throws MojoExecutionException, MojoFailureException {
		try {
			val files = imgDir.list
			if (files == null)
				throw new MojoFailureException("Can't read " + imgDir.canonicalPath)
			var i = 1
			val images = '''
				package quiz.strazacki.app;
				
				import com.google.gwt.core.client.GWT;
				import com.google.gwt.resources.client.ClientBundle;
				import com.google.gwt.resources.client.ImageResource;
				
				public interface Images extends ClientBundle {
					Images INSTANCE = GWT.create(Images.class);
					«FOR f : files»
						@Source("«imgDir.name»/«f»")
						ImageResource img«(i = i + 1)»();
						
					«ENDFOR»		
				}
			'''
			i = 1
			val quizData = '''
				package quiz.strazacki.app
				
				class QuizData {
					static val imgs = Images::INSTANCE
					public static val data = #{
						«FOR f : files»
							imgs.img«(i = i + 1)» -> '«getAnswerFromName(f)»'
						«ENDFOR»
					}
				}
			'''
			println(quizData)

		} catch (IOException e) {
			throw new MojoFailureException(e.toString, e);
		}
	}
	
	val regex = Pattern::compile("-([a-z])[.]")
	def getAnswerFromName(String name) {
		val m = regex.matcher(name.toLowerCase)
		if(!m.find) {
			throw new MojoFailureException("No answer in filename: "+name)
		}
		m.group(1).toUpperCase
	}

	def static void main(String[] args) {
		val g = new GenRes
		g.imgDir = new File('../app/src/main/resources/img')
		g.execute
	}

}
