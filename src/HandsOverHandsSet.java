package linkedlists.lockbased;

import contention.abstractions.AbstractCompositionalIntSet;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementation of the fine grained lock based set.
 * To go through the linked list underlying the set, lock only two nodes at any moment.
 */
public class HandsOverHandsSet extends AbstractCompositionalIntSet {
    // sentinel nodes
    private Node head;
    private Node tail;

    public HandsOverHandsSet(){
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        head.next = tail;
    }

    /**
     * Add an element to the list.
     * If the given element is already in the list, it is not added (no duplicates in a set)
     * @param x the element to add
     * @return a boolean indicating if the element were added
     */
    @Override
    public boolean addInt(int x) {
        head.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock();
            try {
                // go through the list and look for where to insert value
                // lock only two nodes at a time
                while(curr.key < x){
                    pred.unlock();
                    pred=curr;
                    curr=curr.next;
                    curr.lock();
                }
                // the element is already in the set
                if(curr.key == x){
                    return false;
                }
                // insert the element
                Node newNode = new Node(x);
                pred.next = newNode;
                newNode.next=curr;
                return true;
            } finally {
                // realease the locks
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    /**
     * Remove the given element from the set
     * @param x The element to remove
     * @return a boolean indicating if the element where found and removed
     */
    @Override
    public boolean removeInt(int x) {
        head.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock();
            try {
                // go through the list and look for the value to be removed
                // lock only two nodes at a time
                while(curr.key < x){
                    pred.unlock();
                    pred=curr;
                    curr=curr.next;
                    curr.lock();
                }
                // the element is  not in the set
                if(curr.key != x) {
                    return false;
                }
                // remove the element
                pred.next = curr.next;
                return true;
            } finally {
                // release the locks
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    /**
     * Check whether a element is in the set
     * @param x the element to check the presence of
     * @return a boolean indicating if the element is in the set
     */
    @Override
    public boolean containsInt(int x) {
        head.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock();
            try {
                // go through the list and look for the value
                // lock only two nodes at a time
                while(curr.key < x){
                    pred.unlock();
                    pred=curr;
                    curr=curr.next;
                    curr.lock();
                }
                return curr.key == x;
            } finally {
                // release locks
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    /**
     * Non atomic and thread-unsafe
     */
    @Override
    public int size() {
        int count = 0;

        Node curr = head.next;
        while (curr.key != Integer.MAX_VALUE) {
            curr = curr.next;
            count++;
        }
        return count;
    }

    @Override
    public void clear() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }

    private class Node {
        public int key;
        public Node next = null;

        // each node can be locked individually
        private Lock lock = new ReentrantLock();

        Node(int item) {
            key = item;
        }

        void lock(){
            this.lock.lock();
        }

        void unlock(){
            this.lock.unlock();
        }
    }
}
