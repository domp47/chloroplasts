package Assignment3;

import java.io.Serializable;
/**
 * Created by Dom's Computer on 2016-10-30.
 */

public class Queue implements  Serializable{

    private static final long serialVersionUID = -4379690413072425890L;
    private Node first,last;
    private int num;


    public Queue(){
        first = null;
        last = first;
        num = 0;
    }

    public boolean empty() {//checks if queue is empty
        return(num <= 0);
    }

    public int length() {//returns the length of queue
        return num;
    }

    public void enter(String[] s) {//t enter the back of the queue
        if(empty()){
            first = new Node(s, null);
            last = first;
            num = 1;
        }else{
            last.next = new Node(s, null);
            last = last.next;
            num++;
        }
    }

    public String[] leave() {//t leaves the queue
        if(first==null)
            return null;
        String[] s = first.heap;
        first = first.next;
        num--;
        return s;
    }

    public String[] front() {//returns the front t
        if ( num <= 0 ) {
            return null;
        }
        else {
            return first.heap;
        }
    }

}
