package paser;
import implementations.MyStack;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FILE: Parser.java
 * DESCRIPTION:
 * Main application class to read an XML file from the command line,
 * validate its structure using a MyStack, and report errors.
 *
 * NOTE: Adjust the import statements for MyStack based on your project structure.
 */
public class Parser {

    private static final String TAG_REGEX = 
        "(<\\?.*?\\?>)|(<[a-zA-Z0-9]+ *[^/>]*?>)|(</[a-zA-Z0-9]+>)|(<[a-zA-Z0-9]+ *[^>]*?/\\s*>)";
    
    private static final Pattern TAG_PATTERN = Pattern.compile(TAG_REGEX);
    
    // To store errors and print them in order
    private static MyStack<String> errorStack = new MyStack<>();
    
    public static void main(String[] args) {
        
        // 1. Check for Command Line Argument
        if (args.length == 0) {
            System.err.println("Error: Please provide the XML file path as a command-line argument.");
            System.exit(1);
        }

        String filePath = args[0];
        
        // --- Core Parsing Logic ---
        
        MyStack<String> tagStack = new MyStack<>();
        int lineCount = 0;
        boolean hasRoot = false;
        
        System.out.println("--- Starting XML Validation for: " + filePath + " ---");
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                lineCount++;
                Matcher matcher = TAG_PATTERN.matcher(line);
                
                while (matcher.find()) {
                    String fullTag = matcher.group();
                    String tagName = extractTagName(fullTag);

                    if (isProcessingInstruction(fullTag)) {
                        // Ignore processing instructions (e.g., <?xml version="1.0"?>)
                        continue;
                    }
                    
                    if (isSelfClosing(fullTag)) {
                        // Ignore self-closing tags (e.g., <br/>)
                        continue;
                    }

                    if (isOpeningTag(fullTag)) {
                        
                        // Check for root tag rule
                        if (tagStack.isEmpty() && hasRoot) {
                            addError(lineCount, fullTag, "Found more than one root tag.");
                        } else if (tagStack.isEmpty() && !hasRoot) {
                            hasRoot = true;
                        }
                        
                        // Push the opening tag name onto the stack
                        tagStack.push(tagName);
                        
                    } else if (isClosingTag(fullTag)) {
                        
                        // Closing Tag Logic
                        try {
                            String expectedTag = tagStack.pop();
                            
                            // Check for intercrossing or mismatch
                            if (!tagName.equals(expectedTag)) {
                                addError(lineCount, fullTag, "Mismatched closing tag. Expected </" + expectedTag + ">.");
                            }
                        } catch (EmptyStackException e) {
                            // Stack is empty, but we found a closing tag
                            addError(lineCount, fullTag, "Closing tag found with no corresponding opening tag.");
                        }
                    }
                }
            }

            // --- Post-Parsing Validation ---
            
            // 1. Check for unclosed tags
            while (!tagStack.isEmpty()) {
                // Since we don't know the exact line of the error, we report the open tag
                addError(-1, "<" + tagStack.pop() + ">", "Opening tag never closed.");
            }
            
            // 2. Check for missing root (only if no other errors caused premature stack pops)
            if (!hasRoot) {
                 addError(-1, "", "XML document must have exactly one root tag.");
            }
            
        } catch (IOException e) {
            System.err.println("Error: Could not read file at path: " + filePath + " | Details: " + e.getMessage());
            System.exit(1);
        }
        
        // --- Report Results ---
        reportErrors();
    }
    
    /**
     * Extracts the pure tag name (e.g., "root" from "<root>" or "</root>").
     */
    private static String extractTagName(String tag) {
        // Remove <, /, >, ?, and any attributes/whitespace
        String name = tag.replaceAll("<[/]?", "").replaceAll("[/>?].*", "").trim();
        return name;
    }
    
    /**
     * Checks if the tag is a closing tag (e.g., </tag>).
     */
    private static boolean isClosingTag(String tag) {
        return tag.startsWith("</");
    }
    
    /**
     * Checks if the tag is an opening tag (e.g., <tag>).
     */
    private static boolean isOpeningTag(String tag) {
        return tag.startsWith("<") && !tag.startsWith("</") && !tag.endsWith("/>") && !tag.startsWith("<?");
    }

    /**
     * Checks if the tag is a self-closing tag (e.g., <tag/>).
     */
    private static boolean isSelfClosing(String tag) {
        return tag.endsWith("/>") && !tag.startsWith("<?");
    }

    /**
     * Checks if the tag is a processing instruction (e.g., <?xml ... ?>).
     */
    private static boolean isProcessingInstruction(String tag) {
        return tag.startsWith("<?") && tag.endsWith("?>");
    }
    
    /**
     * Stores the error message to be reported later.
     */
    private static void addError(int lineNumber, String tag, String message) {
        String error;
        if (lineNumber == -1) {
             error = String.format("Error: %s [Tag: %s]", message, tag);
        } else {
             error = String.format("Error on line %d: %s [Tag: %s]", lineNumber, message, tag);
        }
        // Push errors onto the stack to reverse the order for printing later (FIFO requirement)
        errorStack.push(error);
    }
    
    /**
     * Prints all errors in the order they occurred.
     */
    private static void reportErrors() {
        if (errorStack.isEmpty()) {
            System.out.println("\n✅ XML Document is PROPERLY CONSTRUCTED.");
        } else {
            System.out.println("\n❌ XML Document is NOT properly constructed. Errors found:");
            // Use another stack to reverse the order of errors for printing
            MyStack<String> printStack = new MyStack<>();
            while(!errorStack.isEmpty()) {
                printStack.push(errorStack.pop());
            }

            while (!printStack.isEmpty()) {
                System.out.println(printStack.pop());
            }
        }
        System.out.println("\n--- Validation Complete ---");
    }
}