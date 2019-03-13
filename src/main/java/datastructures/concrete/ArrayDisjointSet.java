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
            //findSetRecursively(item.hashCode());
            findSetRecursively(getHash(item, pointers.length));
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            //pointers[item.hashCode()] = -1;
            pointers[getHash(item, pointers.length)] = -1;
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
        // return findSetRecursively(item.hashCode());
        return findSetRecursively(getHash(item, pointers.length));
    }

    // return index of representative
    private int findSetRecursively(int index) {
        if (pointers[index] < 0) {
            //return pointers[index];
            return index;
        } else if (pointers[index] != 0) {
            return findSetRecursively(pointers[index]);
        } else {
            throw new IllegalArgumentException();
        }
        // } else  {
        //     return findSetRecursively(pointers[index]);
        // }
    }

    /**
     * Finds the two sets associated with the given items, and combines the two sets together.
     * Does nothing if both items are already in the same set.
     *
     * @throws IllegalArgumentException  if either item1 or item2 is not contained inside this disjoint set
     */
    @Override
    public void union(T item1, T item2) {
        // int rep1 = findSet(item1);
        int index1 = findSetRecursively(getHash(item1, pointers.length));
        // int rep2 = findSet(item2);
        int index2 = findSetRecursively(getHash(item2, pointers.length));
        int rank1 = pointers[index1];
        int rank2 = pointers[index2];

        if (Math.abs(rank1) > Math.abs(rank2)) {
            pointers[index2] = index1;
            //pointers[index1] = pointers[index1] - 1; //
            this.size--;
        } else if (Math.abs(rank2) > Math.abs(rank1)) { // Math.abs(rep1) <= Math.abs(rep2)
            pointers[index1] = index2;
            //pointers[index2] = pointers[index2] - 1; //
            this.size--;
        } else {
            if (index1 > index2) {
                pointers[index2] = index1; // tiebreaker
                pointers[index1] = pointers[index1] - 1;
            } else if (index2 > index1) {
                pointers[index1] = index2; // tiebreaker
                pointers[index2] = pointers[index2] - 1;
            }
            this.size--;
        }
        //throw new NotYetImplementedException();
        // TODO: tieBreaker: Wo Bu Hui Xie Qiu Sun Ke Cheng Lao Da Zhi Dao...
    }

    // private boolean tieBreaker(T item1, T item2) {
    //     // TODO: Wo Bu Hui Xie Qiu Sun Ke Cheng Lao Da Zhi Dao...
    // }

    // TODO: Resizing

    public int size() {
        return size;
    }

    private int getHash(T item, int length) {
        if (item != null) {
            int hash = item.hashCode();
            if (hash < 0) {
                hash = -hash;
                return hash % length;
            } else {
                return hash % length;
            }
        } else {
            return 0;
        }
    }
}
