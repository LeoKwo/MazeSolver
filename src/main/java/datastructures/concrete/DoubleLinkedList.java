package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
//import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see datastructures.interfaces.IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @SuppressWarnings("unchecked")
    public void add(T item) {
        Node newNode = new Node(item);
        if (this.front == null) {
            this.front = newNode;
            this.back = newNode;
        } else {
            newNode.prev = this.back;
            this.back.next = newNode;
            this.back = this.back.next;
        }
        this.size++;
    }

    public T remove() {
        if (back == null) {
            throw new EmptyContainerException();
        } else {
            if (size == 1) {
                T data = this.back.data;
                this.front = null;
                this.back = null;
                size--;
                return data;
            } else {
                T data = this.back.data;
                this.back = this.back.prev;
                size--;
                return data;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        Node cur = getNodeAtIndex(index);
        return (T) cur.data;
    }


    public void set(int index, T item) {
        delete(index);
        insert(index, item);
    }

    @SuppressWarnings("unchecked")
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size() + 1) {
            throw new IndexOutOfBoundsException();
        }

        Node newNode = new Node(item);


        if (this.front == null) {
            this.front = newNode;
            this.back = newNode;
            //this.size++;
        } else if (index == 0) {
            newNode.next = this.front;
            this.front.prev = newNode;
            this.front = newNode;
            //this.size++;
        } else if (index == this.size) {
            newNode.prev = this.back;
            this.back.next = newNode;
            this.back = newNode;
        } else {
            Node cur;
            if ((this.size - index) < index) {
                cur = this.back;
                for (int i = this.size - 1; i > index; i--) {
                    cur = cur.prev;
                }
            } else {
                cur = this.front;
                for (int i = 0; i < index; i++) {
                    cur = cur.next;
                }
            }
            newNode.next = cur;
            newNode.prev = cur.prev;
            cur.prev.next = newNode;
            cur.prev = newNode;
        }
        this.size++;
    }

    @SuppressWarnings("unchecked")
    public T delete(int index) {
        Node cur = getNodeAtIndex(index);
        if (cur == this.front && cur == this.back) {
            this.front = null;
            this.back = null;
            this.size--;
        } else if (cur == this.front) {
            this.front = this.front.next;
            this.front.prev = null;
            this.size--;
        } else if (cur == this.back) {
            this.back = this.back.prev;
            this.back.next = null;
            this.size--;
        } else {
            cur.prev.next = cur.next;
            cur.next.prev = cur.prev;
            this.size--;
        }
        return (T) cur.data;
    }


    public int indexOf(T item) {
        Node cur = front;
        int index = 0;
        while (cur != null) {
            if (item != null && item.equals(cur.data)) {
                return index;
            } else if (cur.data == null) {
                return index;
            }

            cur = cur.next;
            index++;
        }
        return -1;
    }

    public int size() {
        return this.size;
    }


    public boolean contains(T other) {
        int index = indexOf(other);
        return index >= 0;

    }

    private Node getNodeAtIndex(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        Node cur = front;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        return cur;
    }


    public Iterator<T> iterator() {
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (current == null) {
                throw new NoSuchElementException();
            } else {
                if (current.next == null) {
                    T temp = current.data;
                    current = null;
                    return temp;
                } else {
                    current = current.next;
                    return current.prev.data;
                }
            }
        }
    }
}
