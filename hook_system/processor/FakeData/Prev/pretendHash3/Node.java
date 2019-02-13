package Assign1;
import java.io.Serializable;

class Node <E> implements Serializable {
    
    
	private static final long serialVersionUID = 2814694471604259457L;
	E        item;  // the item in the stack
    Node<E>  next;  // the next node in the list
    
    
    
    public Node ( E i, Node<E> n ) {
        
        item = i;
        next = n;
        
    } // constructor
}
