package datastructures.concrete;

import datastructures.interfaces.IDisjointSet;
//import misc.exceptions.NotYetImplementedException;

/**
 * @see IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.
    private int size;

    public ArrayDisjointSet() {
        // TODO: your code here
        this.pointers = new int[100];
        this.size = 0;
    }

    /**
     * Creates a new set containing just the given item.
     * The item is internally assigned an integer id (a 'representative').
     *
     * @throws IllegalArgumentException  if the item is already a part of this disjoint set somewhere
     */
    @Override
    public void makeSet(T item) {
        // throw new NotYetImplementedException();

        try {
            findSetRecursively(item.hashCode());
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            pointers[item.hashCode()] = -1;
            this.size++;
        }
    }

    /**
     * Returns the integer id (the 'representative') associated with the given item.
     *
     * @throws IllegalArgumentException  if the item is not contained inside this disjoint set
     */
    @Override
    public int findSet(T item) {
        // throw new NotYetImplementedException();

        return findSetRecursively(item.hashCode());
    }

    private int findSetRecursively(int index) {
        if (pointers[index] < 0) {
            return pointers[index];
        } else if (pointers[index] != 0) {
            return findSetRecursively(pointers[index]);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Finds the two sets associated with the given items, and combines the two sets together.
     * Does nothing if both items are already in the same set.
     *
     * @throws IllegalArgumentException  if either item1 or item2 is not contained inside this disjoint set
     */
    @Override
    public void union(T item1, T item2) {
        int rep1 = findSet(item1);
        int rep2 = findSet(item2);
        if (Math.abs(rep1) > rep2) {
            pointers[rep2] = rep1;
        } else { // Math.abs(rep1) <= rep2
            pointers[rep1] = rep2;
        }
        //throw new NotYetImplementedException();
        // TODO: iteBreaker: Wo Bu Hui Xie Qiu Sun Ke Cheng Lao Da Zhi Dao...
    }

    // private boolean tieBreaker(T item1, T item2) {
    //     // TODO: Wo Bu Hui Xie Qiu Sun Ke Cheng Lao Da Zhi Dao...
    // }
}
