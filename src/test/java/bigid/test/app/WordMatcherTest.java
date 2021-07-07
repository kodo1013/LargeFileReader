package bigid.test.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import bigid.test.app.agregator.WordLocation;
import bigid.test.app.matcher.WordMatcher;
import junit.framework.TestCase;

public class WordMatcherTest  extends TestCase {

	private static final String testContent = "The Project Gutenberg EBook of The Adventures of Sherlock Holmes\r\n"
			+ "by Sir Arthur Conan Doyle\r\n"
			+ "(#15 in our series by Sir Arthur Conan Doyle)\r\n"
			+ "*****These eBooks Were Prepared By Thousands of Volunteers!*****\r\n"
			+ "\r\n"
			+ "\r\n"
			+ "Title: The Adventures of Sherlock Holmes\r\n"
			+ "\r\n"
			+ "Author: Sir Arthur Conan Doyle";
	private Set<String> wordsToSearch = Stream.of("Arthur", "Sherlock")
			  .collect(Collectors.toCollection(HashSet::new));
	
	@Test
    public void testMatcher() throws InterruptedException{
		Map<String, List<WordLocation>> results = new HashMap<String, List<WordLocation>>();
		Runnable testMatcher = new WordMatcher(wordsToSearch, 0, testContent, (partResults) -> results.putAll(partResults));
		ExecutorService es = Executors.newFixedThreadPool(1);
		es.execute(testMatcher);
		es.shutdown();
		es.awaitTermination(10, TimeUnit.SECONDS);
		assertEquals(2, results.size());
		
		List<WordLocation> arthurLocations = results.get("Arthur");
		List<WordLocation> sherlockLocations = results.get("Sherlock");
		assertEquals(3, arthurLocations.size());
		assertEquals(2, sherlockLocations.size());
		
		WordLocation sherlockLocation = sherlockLocations.get(0);
		assertEquals(1, sherlockLocation.getLineOffset());
		assertEquals(49, sherlockLocation.getCharOffset());
		
		sherlockLocation = sherlockLocations.get(1);
		assertEquals(7, sherlockLocation.getLineOffset());
		assertEquals(25, sherlockLocation.getCharOffset());
	}
}
