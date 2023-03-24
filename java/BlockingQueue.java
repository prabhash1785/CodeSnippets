import java.util.LinkedList;
import java.util.List;

/**
 * Fixed size Bounded Queue.
 */
public class BlockingQueue {

    private int capacity;
    private int size;

    private List<Integer> list;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        this.size = 0;

        this.list = new LinkedList<>();
    }

    /**
     * Add new value to queue. Block if queue is full.
     *
     */
    public synchronized void offer(int value) {
        // block if queue is full
        while (size == capacity) {
            System.out.println("Blocking - Queue is full");
            try {
                wait();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

        list.add(value);
        size++;

        notifyAll();
    }

    /**
     * Remove the oldest element from queue. Block if queue is empty.
     *
     */
    public synchronized int poll() {
        while (size == 0) {
            System.out.println("Blocking - Queue is empty");
            try {
                wait();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }

        int value = list.remove(0);
        size--;

        notifyAll();

        return value;
    }

    public static void main(String[] args) {
        System.out.println("Blocking Queue");

        BlockingQueue queue = new BlockingQueue(3);

        // Thread t1 add elements every 3 seconds to queue
        Thread t1 = new Thread(() -> {
            int count = 10;
            while (count > 0) {
                queue.offer(count);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

                count--;
            }
        }, "T1");

        // Read value every 1 second. Since read is faster than write, queue will block reading thread when
        // queue is empty
        Thread t2 = new Thread(() -> {
            int count = 10;
            while (count > 0) {
                int value = queue.poll();
                System.out.println("Value removed: " + value);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

                count--;
            }
        }, "T2");

        t1.start();
        t2.start();
    }
}
