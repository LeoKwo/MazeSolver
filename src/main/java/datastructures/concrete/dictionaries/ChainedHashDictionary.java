package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int count;

    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.count = 0;
        this.chains = makeArrayOfChains(17);
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    private int getHash(K value, int size) {
        if (value != null) {
            int hash = value.hashCode();
            if (hash < 0) {
                hash = -hash;
                return hash % size;
            } else {
                return hash % size;
            }
        } else {
            return 0;
        }
    }

    @Override
    public V get(K key) {
        int index = getHash(key, chains.length);
        if (containsKey(key)) {
            return chains[index].get(key);
        } else {
            throw new NoSuchKeyException();
        }
    }

    @Override
    public void put(K key, V value) {
        if (count / chains.length >= 0.5) {
            IDictionary<K, V>[] newChains = makeArrayOfChains(2 * chains.length);
            for (IDictionary<K, V> dictionary : chains) {
                if (dictionary != null && dictionary.size() != 0) {
                    for (KVPair<K, V> pair : dictionary) {
                        if (newChains[getHash(pair.getKey(), newChains.length)] == null) {
                            newChains[getHash(pair.getKey(), newChains.length)] = new ArrayDictionary<>();
                        }
                        newChains[getHash(pair.getKey(), newChains.length)].put(pair.getKey(), pair.getValue());
                    }
                }
            }
            chains = newChains;
        }
        int index = getHash(key, chains.length);

        if (chains[index] == null) {
            chains[index] = new ArrayDictionary<>();
        }
        if (!chains[index].containsKey(key)) {
            this.count++;
        }
        chains[index].put(key, value);
    }

    @Override
    public V remove(K key) {
        int index = getHash(key, chains.length);
        if (containsKey(key)) {
            count--;
            return chains[index].remove(key);
        } else {
            throw new NoSuchKeyException();
        }

    }

    @Override
    public boolean containsKey(K key) {
        if (key != null) {
            int index = getHash(key, chains.length);
            if (chains[index] != null) {
                return chains[index].containsKey(key);
            }
            return false;
        } else {
            if (chains[0] != null) {
                return chains[0].containsKey(null);
            }
            return false;
        }
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int index;
        private Iterator<KVPair<K, V>> iterator;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.index = findNext(chains, -1);
            if (index == -2) {
                this.iterator = null;
            } else {
                this.iterator = chains[index].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (iterator == null || !iterator.hasNext()) {
                return findNext(chains, index) > 0;
            } else {
                return true;
            }
        }

        @Override
        public KVPair<K, V> next() {
            if (iterator != null && iterator.hasNext()) {
                return iterator.next();
            } else {
                index = findNext(chains, index);
                if (index != -2) {
                    iterator = chains[index].iterator();
                    return iterator.next();
                } else {
                    throw new NoSuchElementException();
                }
            }
        }

        private int findNext(IDictionary<K, V>[] dictionary, int cur) {
            if (cur != -2) {
                for (int i = cur + 1; i < dictionary.length; i++) {
                    if (dictionary[i] != null && dictionary[i].size() > 0) {
                        return i;
                    }
                }
                return -2;
            } else {
                return -2;
            }
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
