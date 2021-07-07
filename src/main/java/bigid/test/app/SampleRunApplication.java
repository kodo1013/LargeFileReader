package bigid.test.app;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import bigid.test.app.agregator.ResultsHandler;
import bigid.test.app.agregator.WordLocation;
import bigid.test.app.parser.LargeFileParser;

public class SampleRunApplication {

	private static final String LARGE_FILE_URL = "http://norvig.com/big.txt";
	private static final String NAMES_TO_SEARCH = "names_to_search.txt";
	private static final int chunkSize = 1000;
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		LargeFileParser parser = context.getBean(LargeFileParser.class);
		Map<String, List<WordLocation>> results = parser.parse(LARGE_FILE_URL, NAMES_TO_SEARCH, chunkSize);
		ResultsHandler resultsHandler = context.getBean(ResultsHandler.class);
		resultsHandler.saveToFile(results);
	}
}
