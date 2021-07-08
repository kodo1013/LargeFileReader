package bigid.test.app.agregator;

import java.util.List;
import java.util.Map;

public class ConsoleResultsHandler implements ResultsHandler {

	/**
     * Shows parsing results to the console output
     * 
     * @param   results         the Map with results.
     **/
	@Override
	public void displayResults(Map<String, List<WordLocation>> results) {
		if( results != null ) {
			results.forEach( (k, v) -> System.out.println(k + " --> " + v.toString()) );
		}		
	}
}
