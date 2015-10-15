import com.bmuschko.gradle.docker.DockerExtension
import com.bmuschko.gradle.docker.DockerRemoteApiPlugin
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Jar

public class DockerPlugin implements Plugin<Project> {

    def dockerRemoteApi = "http://localhost:2375"
    def registryUrl = "localhost:5043"

    void apply(Project project) {

        project.apply plugin: 'com.bmuschko.docker-remote-api'

        project.docker {
            url = dockerRemoteApi
            registryCredentials {
                url = "https://$registryUrl"
            }
        }

        project.task("copyApp", type:Copy) {
            from project.jar
            into "build/docker/"
        }

        project.task("createDockerfile", type:Dockerfile) {
            dependsOn "copyApp"
            destFile = project.file('build/docker/Dockerfile')
            from("java:8")
            volume("/logs", "/tmp", "/config")
            addFile(project.name, "/${project.name}")
            runCommand("bash -c 'touch /${project.name}'")
            entryPoint("java", "-jar", "/${project.name}")
        }

        project.task("dockerBuildImage", type: DockerBuildImage) {
            dependsOn "createDockerfile"
            inputDir = new File("build/docker")
            tag = "$registryUrl/${project.name}:${project.version}"
        }

        project.task("dockerPushImage", type: DockerPushImage) {
            dependsOn "dockerBuildImage"
            imageName = "$registryUrl/${project.name}"
        }

    }

}

