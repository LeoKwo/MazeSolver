package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see datastructures.interfaces.IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field.
    // We will be inspecting it in our private tests.

    private Pair<K, V>[] elementData;
    private int size;

    // You may add extra fields or helper methods though!

    public ArrayDictionary() {
        this.elementData = makeArrayOfPairs(10);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }


    public V get(K key) {
        V temp = null;
        for (int i = 0; i < size(); i++) {
            if (key != null && key.equals(this.elementData[i].key)) {
                temp = elementData[i].value;
                return temp;
            } else if (elementData[i].key == null) {
                temp = elementData[i].value;
                return temp;
            }
        }
        throw new NoSuchKeyException();
    }

    @SuppressWarnings("unchecked")
    public void put(K key, V value) {
        for (int i = 0; i < this.size; i++) {
            //if key exists
            if (key != null && key.equals(this.elementData[i].key)) {
                elementData[i].value = value;
                return;
                //if key doesn't exist
            } else if (key == null && this.elementData[i].key == null && size < elementData.length) {
                elementData[i].value = value;
                return;
            }
        }
        if (size == elementData.length) {
            elementData = doubleArrLength(elementData);
        }
        this.elementData[size] = new Pair(key, value);
        size++;
    }

    @SuppressWarnings("unchecked")
    private Pair<K, V>[] doubleArrLength(Pair<K, V>[] oldArr) {
        Pair<K, V>[] newArr = makeArrayOfPairs(2 * oldArr.length);
        for (int i = 0; i < oldArr.length; i++) {
            newArr[i] = new Pair(oldArr[i].key, oldArr[i].value);
        }
        return newArr;
    }

    public V remove(K key) {
        for (int i = 0; i < this.size; i++) {
            if (key != null && key.equals(this.elementData[i].key)) {
                V temp = this.elementData[i].value;
                this.elementData[i].key = this.elementData[size - 1].key;
                this.elementData[i].value = this.elementData[size - 1].value;
                size--;
                return temp;
            } else if (elementData[i].key == null) {
                V temp = this.elementData[i].value;
                this.elementData[i] = null;
                size--;
                return temp;
            }
        }
        throw new NoSuchKeyException();
    }

    public boolean containsKey(K key) {
        for (int i = 0; i < this.size; i++) {
            if (key != null && key.equals(this.elementData[i].key)) {
                return true;
            } else if (elementData[i].key == null) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return this.size;
    }

    // Original Implementation
    // @Override
    // public V getOrDefault(K key, V defaultValue) {
    //     if (this.containsKey(key)) {
    //         return this.get(key);
    //     } else {
    //         return defaultValue;
    //     }
    // }
    // Original Implementation
    // Runs in O(n^2)

    // @Override
    // public V getOrDefault(K key, V defaultValue) {
    //
    // }
    //

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }


    @SuppressWarnings("unchecked")
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator(elementData);
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        // Add any fields you need to store state information
        Pair<K, V>[] current;
        int index;

        public ArrayDictionaryIterator(Pair<K, V>[] current) {
            // Initialize the iterator
            this.current = current;
            this.index = 0;
        }

        public boolean hasNext() {
            if (current == null || current.length == 0 || index > current.length) {
                return false;
            }
            try {
                return current[index] != null;
            } catch (Exception e) {
                return false;
            }
        }

        public KVPair<K, V> next() {
            if (hasNext()) {
                index++;
                return new KVPair<>(current[index - 1].key, current[index - 1].value);
            }
            throw new NoSuchElementException();
        }
    }

    @Override
    public V getOrDefault(K key, V defaultValue) {
        try {
            return get(key);
        } catch (NoSuchKeyException e) {
            return defaultValue;
        }
    }
}
