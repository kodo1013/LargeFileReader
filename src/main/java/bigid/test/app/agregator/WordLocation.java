package bigid.test.app.agregator;

public class WordLocation {

	private int lineOffset;
	private int charOffset;
	
	public int getLineOffset() {
		return lineOffset;
	}
	
	public void setLineOffset(int lineOffset) {
		this.lineOffset = lineOffset;
	}
	
	public int getCharOffset() {
		return charOffset;
	}
	
	public void setCharOffset(int charOffset) {
		this.charOffset = charOffset;
	}

	@Override
	public String toString() {
		return "[lineOffset=" + lineOffset + ", charOffset=" + charOffset + "]";
	}
	
}
