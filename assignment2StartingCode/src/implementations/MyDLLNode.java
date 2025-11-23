package implementations;
/**
 * FILE: MyDLLNode.java
 * 
 * Group 9: Jasmine Cheema, Monica Leung, Precious Robert-Ezenta, Mitali Vaid
 * DATE: 2025/11/22
 */

public class MyDLLNode<E> {
	
	E element;
	MyDLLNode<E> next;
	MyDLLNode<E> prev;
	
	public MyDLLNode(E element) {
		
		this.element = element;
		this.next = null;
		this.prev = null;
		
	}
	
	public MyDLLNode(E element, MyDLLNode<E> prev, MyDLLNode<E> next) {
		
		this.element = element;
		this.prev = prev;
		this.next = next;
	}
	
	public E getElement() {
		
		return element;
				
	}
	
	public void setElement(E element) {
		
		this.element = element;
		
	}
	
	public MyDLLNode<E> getNext() {
		
		return next;
		
	}
	
	public void setNext(MyDLLNode<E> next) {
		
		this.next = next;
		
	}
	
	public MyDLLNode<E> getPrev() {
		
		return prev;
				
	}
	
	public void setPrev(MyDLLNode<E> prev) {
		
		this.prev = prev;
		
	}
}
