package glue.controllers;

import glue.entities.TelegrafConfiguration;
import glue.usecases.GenerateConfigurationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telegraf-config")
public class TelegrafConfigController {

    @Autowired
    GenerateConfigurationUseCase useCase;

	@RequestMapping(path="generate", method = RequestMethod.POST, produces = "text/plain")
	public String TelegrafConfigGenerator(@RequestBody TelegrafConfiguration telegrafConfiguration) {
		return useCase.generate(telegrafConfiguration);
	}
}