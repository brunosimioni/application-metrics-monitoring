package glue.entities;

import lombok.Data;

import java.util.ArrayList;

/**
 * Created by bruno on 10/11/16.
 */
@Data
public class DockerConfiguration {
    private String dockerHost;
    private String dockerTlsVerify;
    private String dockerCertPath;
    private String dockerConfigPath;
    private ArrayList<String> dockerContainersNameFilter;
    private String applicationDefaultNetworkName;
}
