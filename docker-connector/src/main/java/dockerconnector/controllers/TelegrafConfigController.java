package dockerconnector.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import dockerconnector.entities.DockerConfiguration;
import dockerconnector.entities.TelegrafConfiguration;
import dockerconnector.repositories.DockerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telegraf-config")
public class TelegrafConfigController {


	@Autowired
	DockerRepository dockerRepository;

	@RequestMapping(path="generate", method = RequestMethod.POST, produces = "text/plain")
	public String TelegrafConfigGenerator(@RequestBody DockerConfiguration dockerConfiguration) {

		TelegrafConfiguration response = new TelegrafConfiguration();

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
        String jolokiaServerTemplate = "" +
                "// container: __cid__" + lf +
                "// internal ip: __internal_ip__" + lf +
                "// image: __image__" + lf +
                "// info: http://__exposed_ip__:__exposed_port__/info" + lf +
                "[[inputs.jolokia.servers]]" + lf +
                "name = \"__servername__\"" + lf +
                "host = \"__exposed_ip__\"" + lf +
                "port = \"__exposed_port__\"" + lf;

        StringBuffer jolokiaServers = new StringBuffer();
        filteredContainers.forEach(c -> jolokiaServers.append(
                jolokiaServerTemplate
                        .replace("__cid__", c.getId())
                        .replace("__internal_ip__", c.getNetworkSettings().getNetworks().get(dockerConfiguration.getApplicationDefaultNetworkName()).getIpAddress())
                        .replace("__image__", c.getImage())
                        .replace("__servername__", c.getNames()[0])
                        .replaceAll("__exposed_ip__", c.getPorts()[0].getIp())
                        .replaceAll("__exposed_port__", String.valueOf(c.getPorts()[0].getPublicPort()))
                + lf + lf

        ));

        return jolokiaServers.toString();
	}
}