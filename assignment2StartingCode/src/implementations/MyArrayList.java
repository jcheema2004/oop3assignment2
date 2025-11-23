package implementations;

import utilities.ListADT;

import java.util.Arrays;

import java.util.NoSuchElementException;

import utilities.Iterator;

public class MyArrayList<E> implements ListADT<E> {

	private static final int DEFAULT_CAPACITY = 10;
	private Object[] elements;
	private int size;

	public MyArrayList() {

		elements = new Object[DEFAULT_CAPACITY];
		size = 0;

	}

	@Override
	public int size() {

		return size;
	}

	@Override
	public void clear() {

		for (int i = 0; i < size; i++) {

			elements[i] = null;

		}

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

		elements[index] = toAdd;
		size++;

		return true;
	}

	@Override
	public boolean add(E toAdd) throws NullPointerException {

		if (toAdd == null)
			throw new NullPointerException("Cannot add null element");

		if (size == elements.length) {

			elements = Arrays.copyOf(elements, elements.length * 2);

		}

		elements[size++] = toAdd;

		return true;
	}

	@Override
	public boolean addAll(ListADT<? extends E> toAdd) throws NullPointerException {

		if (toAdd == null) throw new NullPointerException("cannot add null collection");
		
		boolean modified = false;
		
		for (int i = 0; i < toAdd.size(); i++) {

			add(toAdd.get(i));
			modified = true;
			
		}

		return modified;
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public E get(int index) throws IndexOutOfBoundsException {

		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

		return (E) elements[index];
	}

	@Override
	@SuppressWarnings("unchecked")
	public E remove(int index) throws IndexOutOfBoundsException {
		
		if (index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

		E removed = (E) elements[index];

		// Shifting elements left
		for (int i = index; i < size - 1; i++) {

			elements[i] = elements[i + 1];

		}

		elements[--size] = null;

		return removed;
	}

	@Override
	public E remove(E toRemove) throws NullPointerException {
		
		if (toRemove == null) throw new NullPointerException("Cannot remove null element");
		
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
		
		E old = (E) elements[index];
		
		elements[index] = toChange;
		
		return old;
	}

	@Override
	public boolean isEmpty() {
		
		return size == 0;
	}

	@Override
	public boolean contains(E toFind) throws NullPointerException {
		
		if (toFind == null) throw new NullPointerException("Cannot search for null element");
		
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
		
		if (toHold.length < size) {
			
			toHold = (E[])java.lang.reflect.Array.newInstance(toHold.getClass().getComponentType(), size);
			
		}
		
		System.arraycopy(elements, 0, toHold, 0, size);
		
		if(toHold.length > size) {
			
			toHold[size] = null;
			
		}
		
		return toHold;
	}

	@Override
	public Object[] toArray() {
		
		return Arrays.copyOf(elements, size);
		
	}

	@Override
	public Iterator<E> iterator() {
		
		return new ArrayIterator();
	}
	
	private class ArrayIterator implements Iterator<E> {
		
		private int currentIndex = 0;
		
		@Override
		public boolean hasNext() {
			
			return currentIndex < size;
			
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public E next() throws NoSuchElementException {
			
			if(!hasNext()) throw new NoSuchElementException("No more elements");
			
			return (E) elements[currentIndex++];
			
		}
		
	}
}
