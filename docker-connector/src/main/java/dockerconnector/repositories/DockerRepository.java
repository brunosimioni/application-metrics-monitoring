package dockerconnector.repositories;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import dockerconnector.entities.DockerConfiguration;
import org.springframework.stereotype.Component;

@Component
public class DockerRepository  {

    public DockerClient connect(DockerConfiguration targetConfig) {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(targetConfig.getDockerHost())
                .withDockerTlsVerify(targetConfig.getDockerTlsVerify())
                .withDockerCertPath(targetConfig.getDockerCertPath())
                .withDockerConfig(targetConfig.getDockerConfigPath())
                .build();

        return DockerClientBuilder.getInstance(config).build();
    }
}
