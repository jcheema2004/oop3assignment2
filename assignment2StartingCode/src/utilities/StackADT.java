package utilities;

import java.util.EmptyStackException;
// Assuming Iterator is an interface you created or provided, 
// as used in the test file: import utilities.Iterator;

/**
 * The StackADT interface defines the contract for a Last-In, First-Out (LIFO) 
 * data structure, as required for CPRG 304 Assignment 2.
 * * @param <E> The type of elements held in this stack.
 */
public interface StackADT<E> 
{
    /**
     * Adds an element to the top of the stack.
     * * @param toAdd The element to be added to the stack.
     * @precondition toAdd must not be null.
     * @postcondition The element is added to the top of the stack and the stack's size is increased by one.
     * @throws NullPointerException if the specified element is null.
     */
    void push(E toAdd) throws NullPointerException;

    /**
     * Removes and returns the element at the top of the stack.
     * * @return The element that was at the top of the stack.
     * @precondition The stack must not be empty.
     * @postcondition The element at the top is removed, and the stack's size is decreased by one.
     * @throws EmptyStackException if the stack is empty.
     */
    E pop() throws EmptyStackException;

    /**
     * Returns the element at the top of the stack without removing it.
     * * @return The element at the top of the stack.
     * @precondition The stack must not be empty.
     * @postcondition The stack remains unchanged.
     * @throws EmptyStackException if the stack is empty.
     */
    E peek() throws EmptyStackException;

    /**
     * Removes all elements from the stack.
     * * @postcondition The stack is empty and its size is zero.
     */
    void clear();

    /**
     * Checks if the stack is empty.
     * * @return true if the stack contains no elements, false otherwise.
     */
    boolean isEmpty();

    /**
     * Returns the number of elements in the stack.
     * * @return The size of the stack.
     */
    int size();

    /**
     * Checks if the stack contains the specified element.
     * * @param toFind The element to search for.
     * @return true if the stack contains the element, false otherwise.
     * @precondition toFind must not be null.
     * @throws NullPointerException if the specified element is null.
     */
    boolean contains(E toFind) throws NullPointerException;

    /**
     * Returns the 1-based position from the top of the stack where the element is located.
     * * @param toFind The element to search for.
     * @return The 1-based position of the element from the top, or -1 if the element is not found.
     * @precondition toFind must not be null (implicit, but search usually returns -1 if not found).
     * @postcondition The stack remains unchanged.
     */
    int search(E toFind);

    /**
     * Returns an array containing all of the elements in this stack in top-to-bottom order.
     * * @return An array containing all elements in the stack.
     */
    Object[] toArray();

    /**
     * Returns an array containing all of the elements in this stack in top-to-bottom order;
     * the runtime type of the returned array is that of the specified array. 
     * If the stack fits in the specified array, it is returned therein. 
     * Otherwise, a new array is allocated.
     * * @param toHold The array into which the elements of the stack are to be stored, if it is big enough; otherwise, a new array of the same runtime type is allocated for this purpose.
     * @return An array containing the elements of the stack.
     * @precondition toHold must not be null.
     * @throws NullPointerException if the specified array is null.
     */
    E[] toArray(E[] toHold) throws NullPointerException;

    /**
     * Compares the specified object with this stack for equality. 
     * Returns true if the stacks are the same size and contain the same elements 
     * in the same order (top to bottom).
     * * @param that The object to be compared for equality with this stack.
     * @return true if the specified object is equal to this stack.
     */
    boolean equals(Object that);

    /**
     * Returns an iterator over the elements in this stack in proper sequence (top to bottom).
     * * @return An iterator over the elements in this stack.
     */
    utilities.Iterator<E> iterator();

    /**
     * Indicates whether the stack has exceeded its capacity. 
     * Since this is an array-list-based implementation that should resize, 
     * it is expected to always return false.
     * * @return false, as the stack capacity is not fixed.
     */
    boolean stackOverflow();
}