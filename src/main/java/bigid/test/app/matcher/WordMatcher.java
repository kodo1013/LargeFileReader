package bigid.test.app.matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bigid.test.app.agregator.ResultsCombinerHelper;
import bigid.test.app.agregator.WordLocation;

public class WordMatcher implements Runnable {
	private Set<String> namesToFind;
	private int prevLineNumber;
	private String part;
	private ResultsCombinerHelper combinerHelper;

	public WordMatcher(Set<String> namesToFind, int prevLineNumber, String part,
			ResultsCombinerHelper combinerHelper) {
		super();
		this.namesToFind = namesToFind;
		this.prevLineNumber = prevLineNumber;
		this.part = part;
		this.combinerHelper = combinerHelper;
	}

	@Override
	public void run() {
		Map<String, List<WordLocation>> results = new HashMap<String, List<WordLocation>>();
		for (String name : namesToFind) {
			Pattern p = Pattern.compile("\\b" + name + "\\b");
			Matcher m = p.matcher(part);
			List<WordLocation> linesCounts = results.containsKey(name) ? results.get(name)
					: new ArrayList<WordLocation>();
			while (m.find()) {
				WordLocation coordinates = new WordLocation();
				coordinates.setLineOffset(prevLineNumber + countLines(part.substring(0, m.start())));
				coordinates.setCharOffset(m.start() - part.lastIndexOf("\n", m.start()) - 1);
				linesCounts.add(coordinates);
			}

			if (!linesCounts.isEmpty()) {
				results.put(name, linesCounts);
			}
		}

		combinerHelper.mergeResults(results);
	}

	private int countLines(String s) {
		return s.split("\r\n|\r|\n").length;
	}

}
