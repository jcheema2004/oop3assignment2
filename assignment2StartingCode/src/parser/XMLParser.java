package parser;

import implementations.MyStack;
import utilities.StackADT;
import implementations.MyQueue;
import exceptions.EmptyQueueException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;

/**
 * FILE: XMLParser.java
 * 
 * XML Parser Logic
 *
 * GROUP 9: Jasmine Cheema, Monica Leung, Precious Robert-Ezenta, Mitali Vaid
 * DATE: 2025/11/24
 */
public class XMLParser {

    /**
     * Strict XML-like tag pattern:
     * <tag>, </tag>, <tag ...>, <tag .../>, <tag/>
     * DOES NOT match <>> or stray < or >.
     */
    private static final Pattern TAG_PATTERN = Pattern.compile("</?[A-Za-z_][A-Za-z0-9_\\-]*(\\s+[^<>]*)?/?>");

    /** Inline tags should not drain the whole stack on mismatch */
    private static final Set<String> INLINE_TAGS = new java.util.HashSet<>();
    static {
        INLINE_TAGS.add("i");
        INLINE_TAGS.add("b");
        INLINE_TAGS.add("u");
    }


    private final MyStack<Tag> stack = new MyStack<>();
    private final MyQueue<Tag> errorQ = new MyQueue<>();
    private final MyQueue<Tag> extrasQ = new MyQueue<>();

    

    public void parseFile(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String line;
            int lineNum = 1;

            while ((line = br.readLine()) != null) {
                processLine(line, lineNum);
                lineNum++;
            }

            // At EOF, remaining stack contents are errors
            while (!stack.isEmpty()) {
                try {
                    errorQ.enqueue(stack.pop());
                } catch (Exception ignored) {}
            }

            printResults();

        } catch (IOException e) {
            System.out.println("ERROR: Could not read file: " + path);
        }
    }

    /**
     * Extract real XML tags from line.
     */
    private void processLine(String line, int lineNumber) {
        Matcher m = TAG_PATTERN.matcher(line);

        while (m.find()) {
            String full = m.group();          // full tag "<...>"
            String inside = full.substring(1, full.length() - 1).trim();
//            System.out.println(inside);
            
            handleRawTag(inside, full, lineNumber);
        }
    }

    /**
     * Interpret and classify tags.
     */
    private void handleRawTag(String content, String original, int line) {

        // ignore comments, declarations
        if (content.startsWith("?") || content.startsWith("!"))
            return;

        boolean isEnd = content.startsWith("/");
        boolean isSelfClosing = content.endsWith("/");
        
     // Self-closing → ignore
        if (isSelfClosing) return;

        // Extract tag name
        String name = isEnd
                ? content.substring(1).split("\\s+")[0]
                : content.split("\\s+")[0];
//        System.out.println(name);    

        if (isEnd) {
            processEndTag(new Tag(name, original, line));
        } else {
            stack.push(new Tag(name, original, line));
        }
    }

    /**
     * End-tag processing with corrected inline-tag rule.
     */
    private void processEndTag(Tag endTag) {

        // top of stack matches → pop
        if (!stack.isEmpty() && stack.peek().name.equals(endTag.name)) {
            stack.pop();
            return;
        }

        // if head of errorQ matches → cancel
        try {
            if (!errorQ.isEmpty() && errorQ.peek().name.equals(endTag.name)) {
//                errorQ.dequeue();
                return;
            }
        } catch (EmptyQueueException ignored) {}

        // stack empty → extra closing tag
        if (stack.isEmpty()) {
            try { errorQ.enqueue(endTag); } catch (Exception ignored) {}
            return;
        }

        // Mismatch → walk stack looking for match
        boolean found = stack.search(endTag) != -1;
        MyStack<Tag> temp = new MyStack<>();

        ;
        
        if(found) 
        {
        	while (!stack.isEmpty()) 
        	{
        		 Tag popped = stack.pop();
        		 if (popped.name.equals(endTag.name)) {
        			 break;
        		 }
        		 
        		 try { errorQ.enqueue(popped); } catch (Exception ignored) {}
        		 
//        		 printErrorLine(popped);
        	}
        	
        	
        }
        else 
        {
        	try { extrasQ.enqueue(endTag); } catch (Exception ignored) {}
        }

//        while (!stack.isEmpty()) {
//            Tag popped = stack.pop();
//
//            if (popped.name.equals(endTag.name)) {
//                found = true;
//                break;
//            }
//
//            // NEW RULE:
//            // If popped or end-tag is inline (i, b, u) → do NOT drain entire stack.
//            if (INLINE_TAGS.contains(popped.name) ||
//                INLINE_TAGS.contains(endTag.name)) {
//                try { errorQ.enqueue(popped); } catch (Exception ignored) {}
//                return;
//            }
//
//            // otherwise report popped as error and keep searching
//            try { errorQ.enqueue(popped); } catch (Exception ignored) {}
//        }
//
//        if (!found) {
//            try { extrasQ.enqueue(endTag); } catch (Exception ignored) {}
//        }
    }

    private void printResults() {
        // clean case: both empty
        if (errorQ.isEmpty() && extrasQ.isEmpty()) {
            System.out.println("XML document is constructed correctly.");
            return;
        }

        // If exactly one queue empty, dump both
        if (errorQ.isEmpty() || extrasQ.isEmpty()) {
        	
            try {
                while (!errorQ.isEmpty()) printErrorLine(errorQ.dequeue());
            } catch (EmptyQueueException ignored) {}

            try {
                while (!extrasQ.isEmpty()) printErrorLine(extrasQ.dequeue());
            } catch (EmptyQueueException ignored) {}
            return;
        }
        
//        try
//        {
//        	
//        	 System.out.println("-----------------ERROR----------------");
//             while (!errorQ.isEmpty()) printErrorLine(errorQ.dequeue());
//             System.out.println("-----------------EXTRA----------------");
//             while (!extrasQ.isEmpty()) printErrorLine(extrasQ.dequeue());
//        }
//        catch(EmptyQueueException ignored)
//        {}
       

//        // Both non-empty → reconcile
        try {
            while (!errorQ.isEmpty() && !extrasQ.isEmpty()) {
                Tag error = errorQ.peek();
                Tag extra = extrasQ.peek();

                if (!error.name.equals(extra.name)) {
                    printErrorLine(errorQ.dequeue());
                } else {
                    errorQ.dequeue();
                    extrasQ.dequeue();
                }
            }

            while (!errorQ.isEmpty()) printErrorLine(errorQ.dequeue());
            while (!extrasQ.isEmpty()) printErrorLine(extrasQ.dequeue());

        } catch (EmptyQueueException ignored) {}
    }

    private void printErrorLine(Tag t) {
        String txt = (t.originalText == null || t.originalText.isEmpty())
                ? "<" + t.name + ">"
                : t.originalText;

        System.out.println("Error at line: " + t.lineNumber + " " + txt +
                " is not constructed correctly.");
    }


}
