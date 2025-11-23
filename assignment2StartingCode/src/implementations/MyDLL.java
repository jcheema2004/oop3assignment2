package implementations;

import utilities.ListADT;
import utilities.Iterator;
import java.util.NoSuchElementException;
import java.util.Arrays;

/**
 * FILE: MyDLL.java
 * Doubly Linked List implementation of the ListADT interface.
 * Stores elements in nodes with previous and next references.
 * 
 * GROUP 9: Jasmine Cheema, Monica Leung, Precious Robert-Ezenta, Mitali Vaid
 * DATE: 2025/11/22
 */

public class MyDLL<E> implements ListADT<E> {
	
	private MyDLLNode<E> head;
	private MyDLLNode<E> tail;
	private int size;
	
	public MyDLL() {
		// Clearing references allows garbage collection
		head = null;
		tail = null;
		size = 0;
		
	}

	@Override
	public int size() {
		
		return size;
		
	}

	@Override
	public void clear() {
		
		head = null;
		tail = null;
		size = 0;
		
	}

	@Override
	public boolean add(int index, E toAdd) throws NullPointerException, IndexOutOfBoundsException {
		
		if (toAdd == null) throw new NullPointerException("Cannot add null element");
		if (index < 0 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		
		if (index == 0) {
			// Insert at the beginning
			addToHead(toAdd);
			
		}else if (index == size) {
			// Insert at the end
			add(toAdd);
			
		}else {
			// Insert in the middle: find the node currently at 'index'
			MyDLLNode<E> current = getNode(index);
			// New node inserted between current.prev and current
			MyDLLNode<E> newNode = new MyDLLNode<>(toAdd, current.prev, current);
			
			current.prev.next = newNode;
			current.prev = newNode;
			size++;
			
		}
		
		return true;
	}

	@Override
	public boolean add(E toAdd) throws NullPointerException {
		
		if (toAdd == null) throw new NullPointerException("Cannot add null element");
		
		if(isEmpty()) {
			// Adding first element
			addToHead(toAdd);
		}else {
			// Append to the tail
			MyDLLNode<E> newNode = new MyDLLNode<>(toAdd, tail, null);
			
			tail.next = newNode;
			tail = newNode;
			size++;
		}
		
		return true;
	}

	@Override
	public boolean addAll(ListADT<? extends E> toAdd) throws NullPointerException {
		
		if(toAdd == null) throw new NullPointerException("Cannot add null collection");
		
		boolean modified = false; 
		// Add elements one by one
		for(int i = 0; i < toAdd.size(); i++) {
			
			add(toAdd.get(i));
			modified = true;
		}
		return modified;
	}

	@Override
	public E get(int index) throws IndexOutOfBoundsException {
		// getNode handles bounds checking
		return getNode(index).getElement();
		
	}

	@Override
	public E remove(int index) throws IndexOutOfBoundsException {
		
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: "+ size);
		
		MyDLLNode<E> toRemove = getNode(index);
		
		if(size == 1) {
			// Removing the only element
			head = null;
			tail = null;
		}else if (toRemove == head) {
			// Removing first node
			head = head.next;
			head.prev = null;
		}else if (toRemove == tail) {
			// Removing last node
			tail = tail.prev;
			tail.next = null;
		}else {
			// Removing middle node
			toRemove.prev.next = toRemove.next;
			toRemove.next.prev = toRemove.prev;
			
		}
		
		size--;
		
		return toRemove.getElement();
	}

	@Override
	public E remove(E toRemove) throws NullPointerException {
		
		if (toRemove == null) throw new NullPointerException("Cannot remove null element");
		
		MyDLLNode<E> current = head;
		int index = 0;
		// Sequential search
		while (current != null) {
			
			if(toRemove.equals(current.getElement())) {
				
				return remove(index);
				
			}
			
			current = current.next;
			index++;
			
		}
		
		return null;
	}

	@Override
	public E set(int index, E toChange) throws NullPointerException, IndexOutOfBoundsException {
		
		if (toChange == null) throw new NullPointerException("Cannot set null element");
		
		if (index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
		
		MyDLLNode<E> node = getNode(index);
		E oldElement = node.getElement();
		node.setElement(toChange);
		
		return oldElement;
	}

	@Override
	public boolean isEmpty() {
		
		return size == 0;
	}

	@Override
	public boolean contains(E toFind) throws NullPointerException {
		
		if (toFind == null) throw new NullPointerException("Cannot search for null element");
		
		MyDLLNode<E> current = head;
		while (current != null) {
			
			if(toFind.equals(current.getElement())) {
				
				return true;
				
			}
			
			current = current.next;
		}
		
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public E[] toArray(E[] toHold) throws NullPointerException {
		
		if(toHold == null) throw new NullPointerException("Array cannot be null");
		// Create new array if given one is too small
		if (toHold.length < size) {
			
			toHold = (E[]) java.lang.reflect.Array.newInstance(toHold.getClass().getComponentType(), size);	
			
		}
		// Copy list elements sequentially
		MyDLLNode<E> current = head;
		for (int i = 0; i < size; i++) {
			
			toHold[i] = current.getElement();
			current = current.next;
			
		}
		// Add null terminator if array is larger
		if(toHold.length > size) {
			
			toHold[size] = null;
			
		}
		
		return toHold;
		
	}

	@Override
	public Object[] toArray() {
		
		Object[] array = new Object[size];
		MyDLLNode<E> current = head;
		for (int i = 0; i < size; i++) {
			
			array[i] = current.getElement();
			current = current.next;
			
		}
		
		return array;
		
	}

	@Override
	public Iterator<E> iterator() {
		
		return new DLLIterator();
	}
	
	/**
	 * Adds a new element to the head of the list.
	 */
	private void addToHead(E element) {
		
		MyDLLNode<E> newNode = new MyDLLNode<>(element, null, head);
		
		// Update old head's prev reference
		if(head != null) {
			
			head.prev = newNode;
			
		}
		
		head = newNode;
		
		// If list was empty, tail also becomes head
		if(tail == null) {
			
			tail = head;
			
		}
		size++;
	}
	
	/**
	 * Returns the node at the given index.
	 * Uses optimized traversal: from head for first half, tail for second half.
	 */
	private MyDLLNode<E> getNode(int index){
		
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException("Index: "+index + ", Size: " + size);
		
		MyDLLNode<E> current;
		
		if(index < size /2) {
			
			current = head;
			
			for (int i = 0; i < index; i++) {
				// Search from head
				current = current.next;
				
			}
			
		}else {
			// Search from tail
			current = tail;
			for (int i = size - 1; i > index; i--) {
				
				current = current.prev;
				
			}
			
		}
		
		return current;
		
	}
	
	private class DLLIterator implements Iterator<E> {
		
		private MyDLLNode<E> current = head;
		
		@Override
		public boolean hasNext() {
			
			return current != null;
			
		}
		
		@Override
		public E next() throws NoSuchElementException {
			
			if (!hasNext()) throw new NoSuchElementException("No more elements");
			
			E element = current.getElement();
			current = current.next;
			return element;
			
		}
		
	}

}
