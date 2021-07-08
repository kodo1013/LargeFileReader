package bigid.test.app.agregator;

import java.util.List;
import java.util.Map;

public interface ResultsHandler {
	
	void displayResults( Map<String, List<WordLocation>> results );
}
