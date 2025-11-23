package implementations;

import utilities.ListADT;

import java.util.Arrays;

import java.util.NoSuchElementException;

import utilities.Iterator;

/**
 * FILE: MyArrayList.java
 * A simple ArrayList implementation that grows dynamically.
 * 
 * @param <E> The type of elements stored in this list.
 *
 * GROUP 9: Jasmine Cheema, Monica Leung, Precious Robert-Ezenta, Mitali Vaid
 * DATE: 2025/11/22
 */
public class MyArrayList<E> implements ListADT<E> {

	private static final int DEFAULT_CAPACITY = 10;
	private Object[] elements;
	private int size;
	
    /**
     * Constructs an empty MyArrayList with default capacity.
     */
	public MyArrayList() {

		elements = new Object[DEFAULT_CAPACITY];
		size = 0;

	}

	@Override
	public int size() {
		// Returns the number of elements stored
		return size;
	}

	@Override
	public void clear() {
		// Set all used positions to null for garbage collection
		for (int i = 0; i < size; i++) {
			elements[i] = null;

		}
		// Reset size to 0 — list becomes empty
		size = 0;

	}

	@Override
	public boolean add(int index, E toAdd) throws NullPointerException, IndexOutOfBoundsException {

		if (toAdd == null)
			throw new NullPointerException("cannot add  null element!");
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

		// Ensuring Capacity
		if (size == elements.length) {

			elements = Arrays.copyOf(elements, elements.length * 2);

		}

		// Shifting elements right
		for (int i = size; i > index; i--) {

			elements[i] = elements[i - 1];

		}
		// Insert element
		elements[index] = toAdd;
		size++;

		return true;
	}

	@Override
	public boolean add(E toAdd) throws NullPointerException {

		if (toAdd == null)
			throw new NullPointerException("Cannot add null element");
		// If array is full, double its size
		if (size == elements.length) {
			
			elements = Arrays.copyOf(elements, elements.length * 2);

		}
		// Place element at end
		elements[size++] = toAdd;

		return true;
	}

	@Override
	public boolean addAll(ListADT<? extends E> toAdd) throws NullPointerException {

		if (toAdd == null) throw new NullPointerException("cannot add null collection");
		
		boolean modified = false;
		// Add items one by one
		for (int i = 0; i < toAdd.size(); i++) {

			add(toAdd.get(i));
			modified = true;
			
		}

		return modified;
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public E get(int index) throws IndexOutOfBoundsException {
		// Check bounds
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

		return (E) elements[index];
	}

	@Override
	@SuppressWarnings("unchecked")
	public E remove(int index) throws IndexOutOfBoundsException {
		
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		// Element to return
		E removed = (E) elements[index];

		// Shifting elements left
		for (int i = index; i < size - 1; i++) {

			elements[i] = elements[i + 1];

		}
		// Reduce size and remove leftover reference
		elements[--size] = null;

		return removed;
	}

	@Override
	public E remove(E toRemove) throws NullPointerException {
		
		if (toRemove == null) throw new NullPointerException("Cannot remove null element");
		// Find and remove first occurrence
		for (int i = 0; i < size; i++) {
			
			if(toRemove.equals(elements[i])) {
				return remove(i);
			}
			
		}
		
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public E set(int index, E toChange) throws NullPointerException, IndexOutOfBoundsException {
		
		if (toChange == null) throw new NullPointerException("Cannot set null element");
		
		if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		
		// Store old value
		E old = (E) elements[index];
		
		// Replace with new one
		elements[index] = toChange;
		
		return old;
	}

	@Override
	public boolean isEmpty() {
		// True if list has no elements
		return size == 0;
	}

	@Override
	public boolean contains(E toFind) throws NullPointerException {
		
		if (toFind == null) throw new NullPointerException("Cannot search for null element");
		// Linear search
		for (int i=0; i < size; i++) {
			
			if(toFind.equals(elements[i])) {
				
				return true;
				
			}
			
		}
		
		return false;
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public E[] toArray(E[] toHold) throws NullPointerException {
		
		if(toHold == null) throw new NullPointerException("Array cannot be null");
		// If provided array too small, create new one
		if (toHold.length < size) {
			
			toHold = (E[])java.lang.reflect.Array.newInstance(toHold.getClass().getComponentType(), size);
			
		}
		// Copy elements
		System.arraycopy(elements, 0, toHold, 0, size);
		// Add null terminator if needed
		if(toHold.length > size) {
			
			toHold[size] = null;
			
		}
		
		return toHold;
	}

	@Override
	public Object[] toArray() {
		// Return copy of used portion of internal array
		return Arrays.copyOf(elements, size);
		
	}
	
    /**
     * Inner iterator class to iterate over list elements.
     */
	@Override
	public Iterator<E> iterator() {
		
		return new ArrayIterator();
	}
	
	private class ArrayIterator implements Iterator<E> {
		
		private int currentIndex = 0;
		
		@Override
		public boolean hasNext() {
			// More elements if currentIndex < size
			return currentIndex < size;
			
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public E next() throws NoSuchElementException {
			
			if(!hasNext()) throw new NoSuchElementException("No more elements");
			// Return current and increment index
			return (E) elements[currentIndex++];
			
		}
		
	}
}
