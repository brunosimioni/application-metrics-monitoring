package glue.entities;

import lombok.Data;

/**
 * Created by bruno on 10/11/16.
 */
@Data
public class TelegrafConfiguration {

    private DockerConfiguration dockerConfiguration;
    private String inputHttpJsonName;
}
