package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Stack;

public class StackImpl<T> implements Stack<T> {
    public class Node<T>{
        T element;
        Node next;
    }
    private Node head;
    public StackImpl(){
        //constructor
        this.head = null;
    }
    @Override
    public void push(T element) {
        Node n = new Node();
        n.element = element;
        n.next = this.head;
        this.head = n;
    }

    @Override
    public T pop() {
        if(this.size() == 0){
            return null;
        }
        Node rtn = this.head;
        this.head = this.head.next;
        return (T) rtn.element;
    }

    @Override
    public T peek() {
        if(this.size() == 0){
            return null;
        }
        return (T) this.head.element;
    }

    @Override
    public int size() {
        int counter = 0;
        for(Node x = this.head; x != null; x = x.next){
            counter++;
        }
        return counter;
    }
}
