import java.util.HashMap;
import java.util.Map;

/**
 * Implement a LRU Cache which stores integers as data.
 *
 */
public class LRUCache {

    public static class Node {
        private int key;
        private int value;

        private Node prev;
        private Node next;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node head;
    private Node tail;

    private Map<Integer, Node> map;

    private int capacity;
    private int size;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;

        // sentinel nodes
        this.head = new Node(-1, -1);
        this.tail = new Node(-1, -1);

        this.head.next = this.tail;
        this.tail.prev = this.head;

        this.map = new HashMap<>();
    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }

        Node node = map.get(key);

        removeNode(node);
        moveToHead(node);

        return node.value;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);

            node.value = value;

            // update order
            removeNode(node);
            moveToHead(node);
            return;
        }

        Node data = new Node(key, value);
        map.put(key, data);
        moveToHead(data);

        size++;

        if (size > capacity) {
            removeTailFromCache();
        }
    }

    private void removeNode(Node node) {

        Node prev = node.prev;
        Node next = node.next;

        node.prev =  null;
        node.next = null;

        prev.next = next;
        next.prev = prev;
    }

    private void moveToHead(Node node) {
        Node next = head.next;

        head.next = node;
        node.prev = head;

        node.next = next;
        next.prev = node;
    }

    private void removeTailFromCache() {
        if (size == 0) {
            throw new RuntimeException("Cache is empty");
        }

        // head <> a <> tail
        Node tailNode = tail.prev;

        // remove this from map
        map.remove(tailNode.key);

        tailNode.key = -1;
        tailNode.value = -1;

        tail.prev = null;
        tailNode.next = null;

        tail = tailNode;

        size--;
    }

    public static void main(String[] args) {

        LRUCache cache = new LRUCache(3);

        cache.put(1, 100);

        int key = 1;
        int value = cache.get(key);
        System.out.println("key: " + key + " ;Value: " + value);

        cache.put(2, 200);
        cache.put(3, 300);

        // 5 > 1 > 4

        key = 1;
        value = cache.get(key);
        System.out.println("key: " + key + " ;Value: " + value);

        cache.put(4, 400);

        key = 1;
        value = cache.get(key);
        System.out.println("key: " + key + " ;Value: " + value);

        key = 2;
        value = cache.get(key);
        System.out.println("key: " + key + " ;Value: " + value);

        cache.put(5, 500);

        key = 3;
        value = cache.get(key);
        System.out.println("key: " + key + " ;Value: " + value);

        key = 5;
        value = cache.get(key);
        System.out.println("key: " + key + " ;Value: " + value);
    }
}
