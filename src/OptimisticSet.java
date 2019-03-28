package linkedlists.lockbased;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import contention.abstractions.AbstractCompositionalIntSet;

public class Optimistic extends AbstractCompositionalIntSet {

    // sentinel nodes
    private Node head;
    private Node tail;

    public Optimistic(){     
	  head = new Node(Integer.MIN_VALUE);
	  tail = new Node(Integer.MAX_VALUE);
          head.next = tail;
    }
    
    private class Node {
    	final public int key;
    	public Node next;
    	final private Lock lock;

    	Node(int item) {
    	    key = item;
    	    next = null;
    	    lock = new ReentrantLock();
    	}
    	
    	public void lock() {
		    this.lock.lock();
		}
		
		public void unlock() {
		    this.lock.unlock();
		}
    }

    @Override
    public void clear() {
       head = new Node(Integer.MIN_VALUE);
       head.next = new Node(Integer.MAX_VALUE);
    }
    
    public int size() {
        int count = 0;

        Node curr = head.next;
        while (curr.key != Integer.MAX_VALUE) {
            curr = curr.next;
            count++;
        }
        return count;
    }

	private boolean validate(Node pred, Node curr) {
		Node node=head;
		while(node.key<= pred.key){
			if(node==pred){
				return pred.next==curr;
				}
			node=node.next;
			}
		return false;
	}
	
	public boolean removeInt(int item) {
		while(true){
			Node pred=head;
			Node curr=pred.next;
			while(curr.key<item){
				pred=curr;
				curr=curr.next;
				}
			pred.lock();
			curr.lock();
			try{
				if(validate(pred,curr)){
					if (curr.key==item) {
						pred.next=curr.next;
						return true;
						}
					return false;
					}
				} finally{
					pred.unlock();
					curr.unlock();
					}
			}
	}
	
	public boolean addInt(int item){
		while(true){
			Node pred=head;
			Node curr=pred.next;
			while(curr.key<item){
				pred=curr;
				curr=curr.next;
				}
			pred.lock();
			curr.lock();
			try{
				if(validate(pred,curr)){
					if (curr.key==item){
						return false;
						}
					Node node = new Node(item);
					node.next=curr;
					pred.next=node;
					return true;
					}
				} finally {
					pred.unlock();
					curr.unlock();
					}
			}
	}
	
	public boolean containsInt(int item) {
		while(true){
			Node pred=head;
			Node curr=pred.next;
			while(curr.key<item){
				pred=curr;
				curr=curr.next;
				}
			pred.lock();
			curr.lock();
			try{
				if(validate(pred,curr)){
					return (curr.key==item);
					}
				} finally {
					pred.unlock();
					curr.unlock();
					}
			}
		}	
	
}