package bigid.test.app.agregator;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface ResultsCombinerHelper {

	/**
     * Contains the logic of merging the results of each part
     * 
     * @param   partResults         the Map with results of part matching.
     **/
	void mergeResults(Map<String, List<WordLocation>> partResults);
	
}
