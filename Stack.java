public class Stack<T> {
	
	@SuppressWarnings("hiding")
	protected class StackNode<T> {
		
		T data;
		StackNode<T> next;
		
		public StackNode(T data) {
			
			this.data = data;
			this.next = null;
			
		}
		
		public String toString() {
			
			return "" + data;
			
		}
		
	}
	
	StackNode<T> head;
	
	public Stack() {
		
		head = null;
		
	}
	
	public void push(T data) {
		
		StackNode<T> temp = head;
		StackNode<T> toPush = new StackNode<T>(data);
			
        head = toPush;
        head.next = temp;
		
	}
	
	public T pop() {
		
		StackNode<T> toPop = head;
		
		if (head != null) {
            
            head = head.next;
            return toPop.data;
    
        }
		
        return null;
        
	}
	
	public boolean isEmpty() {
		
		return head == null;
		
	}
	
}
