package bigid.test.app.agregator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public class ResultsHandler {
	private static final Logger LOG = Logger.getLogger(ResultsHandler.class);
	private final static String OUTPUT_FILE_NAME = "results.txt";
	
	/**
     * Shows parsing results to the console output
     * 
     * @param   results         the Map with results.
     **/
	public void displayInConsole( Map<String, List<WordLocation>> results ) {
		if( results != null ) {
			results.forEach( (k, v) -> System.out.println(k + " --> " + v.toString()) );
		}
	}
	
	/**
     * Saves parsing results to the text file
     * 
     * @param   results         the Map with results.
     **/
	public void saveToFile( Map<String, List<WordLocation>> results ) {
		if (results != null) {
			File file = new File(OUTPUT_FILE_NAME);
			try (BufferedWriter bf = new BufferedWriter(new FileWriter(file))) {
				for (Entry<String, List<WordLocation>> entry : results.entrySet()) {
					bf.write(entry.getKey() + " --> " + entry.getValue().toString());
					bf.newLine();
				}
				bf.flush();
			} catch (IOException e) {
				LOG.error("Exception occurred while saving results map", e);
			}
		}
	}
}
