import org.gradle.api.Plugin
import org.gradle.api.Project

public class DockerPlugin implements Plugin<Project> {

    void apply(Project project) {
        def scriptUrl = DockerPlugin.class.getResource("/docker.gradle");
        project.apply from: scriptUrl
    }

}

