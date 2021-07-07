package bigid.test.app.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import bigid.test.app.agregator.WordLocation;
import bigid.test.app.matcher.WordMatcher;

public class LargeFileParser {
	private static final Logger LOG = Logger.getLogger(LargeFileParser.class);
	private static final int THREADS_NUMBER = 5;
	private static final int TIMEOUT = 5;
	
	/**
     * Returns the map with search results
     * 
     * @param   largeFileName         the link to file.
     * @param   namesFile   the file name with names.
     * @param   chunkSize   the parameter indicating the number of lines in each part.
     * @return  the map with search results.
     **/
	public Map<String, List<WordLocation>> parse(String largeFileName, String namesFile, int chunkSize) {
		try {
			URL url = new URL(largeFileName);
			Set<String> searchedNames = resolveSearchedNames(namesFile);
			if (searchedNames != null) {
				Map<String, List<WordLocation>> results = new ConcurrentHashMap<String, List<WordLocation>>();
				try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
					final AtomicInteger partCounter = new AtomicInteger();
					final AtomicInteger linesCounter = new AtomicInteger();
					Stream<String> lines = br.lines();
					ExecutorService es = Executors.newFixedThreadPool(THREADS_NUMBER);
					lines.collect(Collectors.groupingBy(it -> partCounter.getAndIncrement() / chunkSize)).values()
							.forEach(partArray -> {
								String part = partArray.stream().collect(Collectors.joining("\n"));
								es.execute(new WordMatcher(searchedNames, linesCounter.getAndAdd(partArray.size()), part,
										(partResults) -> {
											partResults.forEach((k, v) -> results.merge(k, v, (v1, v2) -> {
												v2.addAll(v1);
												v2.sort(Comparator.comparing(WordLocation::getLineOffset));
												return v2;
											}));
										}));
							});
					es.shutdown();
					es.awaitTermination(TIMEOUT, TimeUnit.MINUTES);
					return results;
				} catch (IOException e) {
					LOG.error("Exception occurred while reading the large file", e);
				} catch (InterruptedException e) {
					LOG.error("Failed to execute word matcher", e);
				}
			}
		} catch (MalformedURLException e) {
			LOG.error("Can't open URL - " + largeFileName, e);
		}
		
		return null;
	}
	
	/**
     * Returns the Set with searched names
     * 
     * @param   namesFile   the file name with names.
     * @return  the Set with searched names.
     **/
	private Set<String> resolveSearchedNames(String namesFile) {
		InputStream is = getClass().getClassLoader().getResourceAsStream(namesFile);
		Set<String> names = new HashSet<String>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] findNames = Arrays.stream(line.split(",")).map(String::trim).toArray(String[]::new);
				Collections.addAll(names, findNames);
			}
		} catch (IOException e) {
			LOG.error("Exception occurred while reading the file with names", e);
		}

		return names;
	}
	
	
}
