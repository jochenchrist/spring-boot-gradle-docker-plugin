import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertTrue

class DockerPluginTest {
    @Test
    public void canAddTaskToProject() {
        Project project = ProjectBuilder.builder().build()
        def task = project.task('dockerBuildImage', type: DockerBuildImage)
        assertTrue(task instanceof DockerBuildImage)
    }
}
