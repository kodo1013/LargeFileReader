package bigid.test.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import bigid.test.app.agregator.ResultsHandler;
import bigid.test.app.parser.LargeFileParser;

@Configuration
public class AppConfig {

	@Bean
	public LargeFileParser largeFileParser() {
		return new LargeFileParser();
	}
	
	@Bean
	public ResultsHandler resultsHandler() {
		return new ResultsHandler();
	}
}
