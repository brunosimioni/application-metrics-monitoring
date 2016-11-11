package glue.usecases;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import glue.entities.DockerConfiguration;
import glue.entities.TelegrafConfiguration;
import glue.repositories.DockerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bruno on 11/11/16.
 */
@Component
public class GenerateConfigurationUseCase
{
    @Autowired
    DockerRepository dockerRepository;

    public String generate(TelegrafConfiguration telegrafConfiguration)
    {
        DockerConfiguration dockerConfiguration = telegrafConfiguration.getDockerConfiguration();

        DockerClient client = dockerRepository.connect(dockerConfiguration);
        List<Container> allContainers = client.listContainersCmd().exec();
        List<Container> filteredContainers = allContainers.stream().filter((c) -> {
            boolean found = false;
            for (String s : dockerConfiguration.getDockerContainersNameFilter()) {
                String containerName = c.getNames()[0];

                if (containerName.indexOf(s) > -1)
                    found = true;
            };
            return found;
        }).collect(Collectors.toList());

        String lf = System.lineSeparator();
        String inputTemplate = "" +
                "[[inputs.httpjson]]" + lf +
                "name = \"__name__\"" + lf +
                "servers = __servers__ " + lf +
                "method = \"GET\"" + lf;


        String serverTemplate = "\"http://__ip__:__port__/metrics/.*response.*|.*counter.*\"";
        ArrayList<String> servers = new ArrayList<>();

        filteredContainers.forEach(c -> servers.add(
                serverTemplate
                        .replace("__ip__", c.getPorts()[0].getIp())
                        .replace("__port__", String.valueOf(c.getPorts()[0].getPublicPort()))
        ));

        return inputTemplate
                    .replace("__servers__", Arrays.toString(servers.toArray()))
                    .replace("__name__", telegrafConfiguration.getInputHttpJsonName());
    }
}
