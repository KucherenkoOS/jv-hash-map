package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int RESIZE_MULTIPLIER = 2;

    private Node<K, V>[] table;
    private int size = 0;
    private int threshold;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    @Override
    public void put(K key, V value) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;
        Node<K, V> head = table[index];

        for (Node<K, V> node = head; node != null; node = node.getNext()) {
            if (node.getHash() == hash && Objects.equals(node.getKey(), key)) {
                node.setValue(value);
                return;
            }
        }

        Node<K, V> newNode = new Node<>(key, value, hash, head);
        table[index] = newNode;
        size++;

        if (size >= threshold) {
            resize();
        }
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = (table.length - 1) & hash;

        for (Node<K, V> node = table[index]; node != null; node = node.getNext()) {
            if (node.getHash() == hash && Objects.equals(node.getKey(), key)) {
                return node.getValue();
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * RESIZE_MULTIPLIER; // [✔ USED NAMED CONSTANT]
        Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
        threshold = (int) (newCapacity * LOAD_FACTOR);

        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.getNext();
                int index = (newCapacity - 1) & node.getHash();
                node.setNext(newTable[index]);
                newTable[index] = node;
                node = next;
            }
        }
        table = newTable;
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private final int hash;
        private Node<K, V> next;

        private Node(K key, V value, int hash, Node<K, V> next) { // [✔ EXPLICIT ACCESS MODIFIER]
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        private K getKey() {
            return key;
        }

        private V getValue() {
            return value;
        }

        private void setValue(V value) {
            this.value = value;
        }

        private int getHash() {
            return hash;
        }

        private Node<K, V> getNext() {
            return next;
        }

        private void setNext(Node<K, V> next) {
            this.next = next;
        }
    }
}
