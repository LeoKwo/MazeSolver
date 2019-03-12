package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Feel free to add more fields and constants.

    // Added more fields
    private int size;
    // private Heap<T> min;
    // private Heap<T> lastAdded;
    // Added more fields

    public ArrayHeap() {
        // throw new NotYetImplementedException();

        // Modified
        this.heap = makeArrayOfT(17);        // Maybe questionable
        this.size = 0;
        // this.min = null;                             // This too
        // Modified
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    @Override
    public T removeMin() {
        // throw new NotYetImplementedException();

        // unchecked tho
        if (this.size != 0) {
            T data = this.heap[0];
            this.heap[0] = this.heap[this.size - 1];
            this.size--;
            percolateDown(0);
            //this.size--;
            return data;

        } else {
            throw new EmptyContainerException();
        }
        // unchecked tho
    }

    @Override
    public T peekMin() {
        if (this.size == 0) {
            throw new EmptyContainerException();
        } else {
            return this.heap[0];
        }
        //return this.heap[0];
    }

    @Override
    public void insert(T item) {
        // throw new NotYetImplementedException();
        if (item == null) {
            throw new IllegalArgumentException();
        } else {
            if (size / this.heap.length >= 0.5) {
                T[] newHeap = makeArrayOfT(2 * this.heap.length);
                for (int i = 0; i < this.heap.length; i++) {
                    newHeap[i] = this.heap[i];
                }
                this.heap = newHeap;
            }
            this.heap[this.size] = item;
            percolateUp(this.size);
            this.size++;
            // percolateUp
        }
    }

    @Override
    public int size() {
        // throw new NotYetImplementedException();
        return this.size;
    }

    private void percolateDown(int index) {
        int smallest = findSmallest(index);
        if (smallest != -1) {
            if (this.heap[smallest].compareTo(this.heap[index]) <= 0) {
                T temp = this.heap[smallest];
                this.heap[smallest] = this.heap[index];
                this.heap[index] = temp;
                percolateDown(smallest);
            }
        }
    }

    private int findSmallest(int index) {
        if (4 * index + 1 <= this.size - 1) {
            T min = this.heap[4 * index + 1];
            int minIndex = 4 * index + 1;
            for (int i = 2; i <= 4; i++) {
                if (4 * index + i <= this.size - 1) {
                    if (min.compareTo(this.heap[4 * index + i]) >= 0) {
                        min = this.heap[4 * index + i];
                        minIndex = 4 * index + i;
                    }
                }
            }
            return minIndex;
        } else {
            return -1;
            // Means no child
        }
    }

    private void percolateUp(int index) {
        if (this.heap[(index - 1) / 4].compareTo(this.heap[index]) >= 0) {
            T temp = this.heap[(index - 1) / 4];
            this.heap[(index - 1) / 4] = this.heap[index];
            this.heap[index] = temp;
            if (index != 0) {
                percolateUp((index - 1) / 4);
            }
        }
    }

    public String toString() {
        String data = "";
        for (int i = 0; i < size; i++) {
            data += " " + this.heap[i].toString();
        }
        return data;
    }
    // resize
}
