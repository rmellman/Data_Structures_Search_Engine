package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;

import java.util.NoSuchElementException;

public class MinHeapImpl<E extends Comparable<E>> extends MinHeap<E> {
    public MinHeapImpl(){
        this.elements = (E[]) new Comparable[1000];
    }
    @Override
    protected void doubleArraySize() {
        E[] doubler = (E[]) new Comparable[this.elements.length * 2];
        for(int i = 0; i < this.elements.length; i++){
            doubler[i] = (E) this.elements[i];
        }
        this.elements = doubler;
    }
    @Override
    public void reHeapify(E element) {
        //what is this comparable shtick??
        int index = getArrayIndex(element);
        if(index != -1) {
            upHeap(index);
            downHeap(index);
        } else if (index == -1) {
            throw new NoSuchElementException();
        }
    }
    @Override
    protected int getArrayIndex(E element) {
        for(int i = 1; i < this.elements.length - 1; i++){
            if(this.elements[i] != null) {
                //might need to do a compare to here
                //System.out.println(i);
                if (this.elements[i].compareTo(element) == 0) {
                    //System.out.println("got here");
                    return i;
                }
            }
        }
        return -1;
    }
}
