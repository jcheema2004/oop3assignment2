package paser;

import implementations.MyStack;
import implementations.MyQueue;
// IMPORT: This must match the exception thrown by your MyQueue.dequeue()
import exceptions.EmptyQueueException; 
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
 * validate its structure using a MyStack, and report errors using a MyQueue.
 */
public class Parser {

    private static final String TAG_REGEX = 
        // 1. Processing Instruction: <?xml ... ?>
        "(<\\?.*?\\?>)|" + 
        // 2. Opening Tag (potentially with attributes): <tag attr="value">
        "(<[a-zA-Z0-9]+ *[^/>]*?>)|" + 
        // 3. Closing Tag: </tag>
        "(</[a-zA-Z0-9]+>)|" + 
        // 4. Self-Closing Tag: <tag/>
        "(<[a-zA-Z0-9]+ *[^>]*?/\\s*>)";
    
    private static final Pattern TAG_PATTERN = Pattern.compile(TAG_REGEX);
    
    // Using MyQueue to store errors in the order they occur (FIFO)
    private static MyQueue<String> errorQueue = new MyQueue<>();
    
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

                    if (isProcessingInstruction(fullTag) || isSelfClosing(fullTag)) {
                        // Ignore processing instructions and self-closing tags
                        continue;
                    }
                    
                    if (isOpeningTag(fullTag)) {
                        
                        // Rule: one and only one root tag.
                        if (tagStack.isEmpty() && hasRoot) {
                            addError(lineCount, fullTag, "Found more than one top-level root element.");
                        } else if (tagStack.isEmpty() && !hasRoot) {
                            hasRoot = true;
                        }
                        
                        // Push the opening tag name onto the stack
                        tagStack.push(tagName);
                        
                    } else if (isClosingTag(fullTag)) {
                        
                        // Closing Tag Logic
                        try {
                            String expectedTag = tagStack.pop();
                            
                            // Check for mismatched tags or intercrossing
                            if (!tagName.equals(expectedTag)) {
                                addError(lineCount, fullTag, "Mismatched closing tag. Expected </" + expectedTag + ">.");
                            }
                        } catch (EmptyStackException e) {
                            // Closing tag found with no corresponding opening tag
                            addError(lineCount, fullTag, "Closing tag found with no corresponding opening tag.");
                        }
                    }
                }
            }

            // --- Post-Parsing Validation ---
            
            // 1. Check for unclosed tags
            while (!tagStack.isEmpty()) {
                String unclosedTag = tagStack.pop();
                addError(-1, "<" + unclosedTag + ">", "Opening tag never closed.");
            }
            
            // 2. Final check for missing root 
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
     * Stores the error message to be reported later using the MyQueue (FIFO).
     */
    private static void addError(int lineNumber, String tag, String message) {
        String error;
        if (lineNumber == -1) {
             error = String.format("Error: %s [Tag: %s]", message, tag);
        } else {
             error = String.format("Error on line %d: %s [Tag: %s]", lineNumber, message, tag);
        }
        // Enqueue errors to preserve the order they occurred (FIFO)
        errorQueue.enqueue(error);
    }
    
    /**
     * Prints all errors in the order they occurred by dequeuing from the MyQueue.
     */
    private static void reportErrors() {
        if (errorQueue.isEmpty()) {
            System.out.println("\nXML document is constructed correctly.");
        } else {
            System.out.println("\n❌ XML Document is NOT properly constructed. Errors found:");
            
            // Dequeue and print errors directly in FIFO order.
            while (!errorQueue.isEmpty()) { 
                try {
                    System.out.println(errorQueue.dequeue());
                } catch (EmptyQueueException e) { // <-- FIX APPLIED HERE
                    // The while loop condition should prevent this, but it handles the exception thrown by your ADT.
                    break; 
                }
            }
        }
        System.out.println("\n--- Validation Complete ---");
    }
}