
package Assignment3;

import java.io.Serializable;
/**
 * Created by Dom's Computer on 2016-10-30.
 */

class Node implements Serializable {


    private static final long serialVersionUID = 2814694471604259457L;
    String[]       heap;  // the item in the stack
    Node  next;  // the next node in the list

    public Node ( String[] i, Node n ) {
        heap = i;
        next = n;
    } // constructor
}
