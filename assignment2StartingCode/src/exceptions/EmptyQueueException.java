package exceptions;

/**
 * EmptyQueueException.java
 *
 * Thrown to indicate that a queue operation requiring one or more elements
 * was attempted on an empty queue.
 *
 * @author Group 9
 * @version 1.0
 */
public class EmptyQueueException extends RuntimeException {

    /**
     * Constructs a new EmptyQueueException with no detail message.
     */
    public EmptyQueueException() {
        super("Queue is empty.");
    }

    /**
     * Constructs a new EmptyQueueException with the specified detail message.
     *
     * @param message the detail message.
     */
    public EmptyQueueException(String message) {
        super(message);
    }
}