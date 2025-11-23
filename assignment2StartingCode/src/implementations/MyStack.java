package implementations;

import utilities.StackADT;
import utilities.Iterator;
import java.util.EmptyStackException;
import java.util.NoSuchElementException;
import java.util.Arrays;

public class MyStack<E> implements StackADT<E> {
		
	private MyArrayList<E> list;
	
	public MyStack() {
		
		list = new MyArrayList<>();
		
	}

	@Override
	public void push(E toAdd) throws NullPointerException {
		
		if(toAdd == null) {
			
			throw new NullPointerException("Cannot push null elemnt");
			
		}
		
		list.add(toAdd);
		
	}

	@Override
	public E pop() throws EmptyStackException {
		
		if(isEmpty()) {
			
			throw new EmptyStackException();
			
		}
		
		return list.remove(list.size() - 1);
	}

	@Override
	public E peek() throws EmptyStackException {
		
		if(isEmpty()) {
			
			throw new EmptyStackException();
			
		}
		
		return list.get(list.size() - 1);
		
	}

	@Override
	public void clear() {
		
		list.clear();
		
	}

	@Override
	public boolean isEmpty() {
		
		return list.isEmpty();
	}

	@Override
	public Object[] toArray() {
		
		//Returning in stack order (top to bottom)....
		Object[] array = list.toArray();
		//reversing array to get top-first order....
		Object[] reversed = new Object[array.length];
		
		for (int i = 0; i < array.length; i++) {
			
			reversed[i] = array[array.length - 1 -i];
			
		}
		
		return reversed;
	}

	@Override
	@SuppressWarnings("unchecked")
	public E[] toArray(E[] holder) throws NullPointerException {
		
		if(holder == null) {
			
			throw new NullPointerException("Array cannot be null");
			
		}
		
		Object[] stackArray = toArray();
		
		if(holder.length < stackArray.length) {
			
			holder = (E[]) java.lang.reflect.Array.newInstance(holder.getClass().getComponentType(), stackArray.length);
			
		}
		
		System.arraycopy(stackArray, 0, holder, 0, stackArray.length);
		
		if(holder.length > stackArray.length) {
			
			holder[stackArray.length] = null;
			
		}
		
		return holder;
	}

	@Override
	public boolean contains(E toFind) throws NullPointerException {
		
		if(toFind == null) {
			
			throw new NullPointerException("Cannot search for null element");
			
		}
		
		return list.contains(toFind);
	}

	@Override
	public int search(E toFind) {
		
		for (int i = list.size() - 1, position = 1; i >= 0; i--, position++) {
			
			if (toFind.equals(list.get(i))) {
				
				return position;
				
			}
			
		}
		return -1;
	}

	@Override
	public Iterator<E> iterator() {
		
		return new StackIterator();
		
	}

	@Override
	public boolean equals(StackADT<E> that) {
		
		if (that == null || this.size() != that.size()) {
			
			return false;
			
		}
		
		//converting both stacks to arrays and compare....
		Object[] thisArray = this.toArray();
		Object[] thatArray = that.toArray();
		
		return Arrays.equals(thisArray, thatArray);
		
	}

	@Override
	public int size() {
		
		return list.size();
		
	}

	@Override
	public boolean stackOverflow() {
		//As we are using arrayList which resizes, stack never overflows....
		return false;
		
	}
	
	private class StackIterator implements Iterator<E>{
		
		private int currentIndex;
		
		public StackIterator() {
			
			currentIndex = list.size() -1; 
			
		}
		
		@Override
		public boolean hasNext() {
			
			return currentIndex >= 0;
			
		}
		
		@Override
		public E next() throws NoSuchElementException{
			
			if(!hasNext()) {
				
				throw new NoSuchElementException("No more elements in stack !");
				
			}
			return list.get(currentIndex--);
			
		}
	}
	
	
	
}
