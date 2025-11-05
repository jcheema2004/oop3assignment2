package utilities;

import exceptions.EmptyQueueException;

/**
 * QueueADT.java
 *
 *
 * @param <E> The type of elements stored in this queue.
 *
 * @author Group 9
 * @version 1.0
 */
public interface QueueADT<E> extends Iterable<E> {

    /**
     * Adds an element to the tail (end) of the queue.
     *
     * @param toAdd the element to add (must not be null)
     * @throws NullPointerException if the specified element is null
     */
    void enqueue(E toAdd) throws NullPointerException;

    /**
     * Removes and returns the element at the head (front) of the queue.
     *
     * @return the element removed from the head
     * @throws EmptyQueueException if the queue is empty
     */
    E dequeue() throws EmptyQueueException;

    /**
     * Returns (but does not remove) the element at the head (front) of the queue.
     *
     * @return the first element in the queue
     * @throws EmptyQueueException if the queue is empty
     */
    E peek() throws EmptyQueueException;

    /**
     * Removes all elements from the queue, leaving it empty.
     */
    void dequeueAll();

    /**
     * Checks whether the queue is empty.
     *
     * @return true if the queue contains no elements, false otherwise
     */
    boolean isEmpty();

    /**
     * Checks whether the queue is full.
     *
     * @return true if the queue cannot accept more elements; false otherwise
     */
    boolean isFull();

    /**
     * Returns the number of elements currently stored in the queue.
     *
     * @return the size of the queue
     */
    int size();

    /**
     * Determines whether this queue contains the specified element.
     *
     * @param toFind the element to locate (must not be null)
     * @return true if the element exists in the queue, false otherwise
     * @throws NullPointerException if the specified element is null
     */
    boolean contains(E toFind) throws NullPointerException;

    /**
     * Searches for the specified element in the queue.
     *
     * @param toFind the element to locate (must not be null)
     * @return the 1-based position of the element in the queue, or -1 if not found
     */
    int search(E toFind);

    /**
     * Returns an array containing all elements in this queue, from head to tail.
     *
     * @return an array of all queue elements
     */
    Object[] toArray();

    /**
     * Returns an array containing all elements in this queue, from head to tail.
     * The runtime type of the returned array matches that of the specified array.
     *
     * @param holder the array into which the elements are to be stored,
     *               if it is large enough; otherwise, a new array is created.
     * @param <T>    the runtime type of the array to contain the elements
     * @return an array containing the elements of the queue
     * @throws NullPointerException if the specified array is null
     */
    <T> T[] toArray(T[] holder) throws NullPointerException;

    /**
     * Compares this queue to another queue for equality.
     * Two queues are equal if they contain the same elements in the same order.
     *
     * @param that the other queue to compare
     * @return true if both queues are equal, false otherwise
     */
    boolean equals(QueueADT<E> that);
}
