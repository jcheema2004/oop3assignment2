package parser;



public class Tag {
    final String name;
    final String originalText;
    final int lineNumber;

    Tag(String name, String originalText, int lineNumber) {
        this.name = name;
        this.originalText = originalText;
        this.lineNumber = lineNumber;
    }
    
    @Override
	public boolean equals(Object target) {
		Tag container = (Tag) target;
    	return this.name.equals(container.name);
    }
}


