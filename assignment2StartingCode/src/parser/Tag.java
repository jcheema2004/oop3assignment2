package parser;

/**
 * FILE: Tag.java
 * 
 * Tag class
 *
 * GROUP 9: Jasmine Cheema, Monica Leung, Precious Robert-Ezenta, Mitali Vaid
 * DATE: 2025/11/24
 */

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


