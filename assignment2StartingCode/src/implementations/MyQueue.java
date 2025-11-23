package implementations;

import utilities.QueueADT;
import utilities.Iterator;
import exceptions.EmptyQueueException;
import java.util.NoSuchElementException;
import java.util.Arrays;

public class MyQueue<E> implements QueueADT<E> {
	
	private MyDLL<E> list;
	
	public MyQueue() {
		
		list = new MyDLL<>();
		
	}

	@Override
	public void enqueue(E toAdd) throws NullPointerException {
		
		if (toAdd == null) {
			
			throw new NullPointerException("Cannot enqueue null element");
			
		}
		
		list.add(toAdd);
		
	}

	@Override
	public E dequeue() throws EmptyQueueException {
		
		if (isEmpty()) {
			
			throw new EmptyQueueException("Queue is empty!");
			
		}
		
		return list.remove(0);
		
	}

	@Override
	public E peek() throws EmptyQueueException {
		
		if  (isEmpty()) {
			
			throw new EmptyQueueException("Queue is empty");
			
		}
		
		return list.get(0);
		
	}

	@Override
	public void dequeueAll() {
		
		list.clear();
		
	}

	@Override
	public boolean isEmpty() {
		
		return list.isEmpty();
	}

	@Override
	public boolean contains(E toFind) throws NullPointerException {
		
		if (toFind == null) {
			
			throw new NullPointerException("Cannot search for null element");
			
		}
		
		return list.contains(toFind);
		
	}

	@Override
	public int search(E toFind) {
		// Searching from front, position 1 to rear..
		for (int i = 0; i < list.size(); i++) {
			
			if(toFind.equals(list.get(i))) {
				
				return i + 1;
				
			}
			
		}
		
		return -1;
		
	}

	@Override
	public Iterator<E> iterator() {
		
		return new QueueIterator();
		
	}

	@Override
	public boolean equals(QueueADT<E> that) {
		
		if (that == null || this.size() != that.size()) {
			
			return false;
			
		}
		
		Object[] thisArray = this.toArray();
		Object[] thatArray = that.toArray();
		
		return Arrays.equals(thisArray, thatArray);
		
	}

	@Override
	public Object[] toArray() {
		
		return list.toArray();
		
	}

	@Override
	@SuppressWarnings("unchecked")
	public E[] toArray(E[] holder) throws NullPointerException {
		
		if (holder == null) {
			
			throw new NullPointerException("Array cannot be null");
			
		}
		
		return list.toArray(holder);
		
	}

	@Override
	public boolean isFull() {
		//As we are using DLL which has like no fixed size, queue is never full....
		return false;
	}

	@Override
	public int size() {
		
		return list.size();
	}
	
	private class QueueIterator implements Iterator<E> {
		
		private int currentIndex;
		
		public QueueIterator() {
			
			currentIndex = 0;
			
		}
		
		@Override
		public boolean hasNext() {
			
			return currentIndex < list.size();
			
		}
		
		@Override
		public E next() throws NoSuchElementException{ 
			
			if (!hasNext()) {
				
				throw new NoSuchElementException("No more elements in queue!");
				
			}
			
			return list.get(currentIndex++);
			
		}
		
	}
	
	
}
