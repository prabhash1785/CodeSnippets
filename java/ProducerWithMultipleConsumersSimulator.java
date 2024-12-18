import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Simulation of a Producer with multiple consumers.
 *
 * A single producer adds users with unique id to a queue every 2 seconds. While there are
 * 2 agents who handle these added users from queue. Each agent takes 4 seconds to handle each user's request. There
 * are a total of 6 users to be handled.
 *
 * Produce a simulation of messages saying - User added, User handled per user and all users done when simulation
 * completes.
 *
 * Important note - User added message for any user "N" must appear before message saying "handling user N".
 *
 * @author Prabhash Rathore
 *
 */
public class ProducerWithMultipleConsumersSimulator {

    public static class User {
        private int id;

        public User(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "id: " + id;
        }
    }

    // Blocking Queue is used to make sure consumer block until user is added to queue
    private BlockingQueue<User> queue;
    private CountDownLatch countDownLatch;

    private int maxUsers;
    private int timeIntervalToAddUserInSec;
    private int timeIntervalToHanldeUserReqInSec;

    public ProducerWithMultipleConsumersSimulator() {
        this.queue = new LinkedBlockingQueue<>();

        this.maxUsers = 6;
        this.timeIntervalToAddUserInSec = 2;

        this.timeIntervalToHanldeUserReqInSec = 4;

        this.countDownLatch = new CountDownLatch(maxUsers);
    }

    /**
     * Runnable task to add users at given interval
     */
    public static class AddUserRunnable implements Runnable {

        private BlockingQueue<User> queue;
        private int maxUsers;
        private int timeIntervalToAddUserInSec;

        public AddUserRunnable(BlockingQueue<User> queue, int maxUsers, int timeIntervalToAddUserInSec) {
            this.queue = queue;
            this.maxUsers = maxUsers;
            this.timeIntervalToAddUserInSec = timeIntervalToAddUserInSec;
        }

        @Override
        public void run() {
            for (int i = 1; i <= maxUsers; i++) {
                User user = new User(i);
                try {

                    // Synchronization is needed to create message sequencing, i.e., this is needed to make sure
                    // added user message of producer appears before handle user message of consumer
                    synchronized (user) {
                        queue.put(user);
                        System.out.println(Thread.currentThread().getName() + " -> Added user id: " + i);
                    }

                    Thread.sleep(timeIntervalToAddUserInSec * 1000); // delay
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }

    public static class HandleUserReqRunnable implements Runnable {
        private BlockingQueue<User> queue;
        private CountDownLatch countDownLatch;
        private int timeIntervalToHanldeUserReqInSec;

        public HandleUserReqRunnable(BlockingQueue<User> queue, int timeIntervalToHanldeUserReqInSec,
                                     CountDownLatch countDownLatch) {
            this.queue = queue;
            this.timeIntervalToHanldeUserReqInSec = timeIntervalToHanldeUserReqInSec;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            while(countDownLatch.getCount() > 0) {

                try {
                    User user = queue.take(); // block here until queue has users

                    // Synchronized to make sure below message is printed only after Producer thread prints
                    // it's message
                    synchronized (user) {
                        System.out.println(Thread.currentThread().getName() + " -> Handling request for user id: "
                                + user.id);
                    }

                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }

                try {
                    Thread.sleep(timeIntervalToHanldeUserReqInSec * 1000);
                } catch(InterruptedException ie) {
                    ie.printStackTrace();
                }

                countDownLatch.countDown();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Simulation of a Producer with multiple consumers ====\n");

        ProducerWithMultipleConsumersSimulator simulator = new ProducerWithMultipleConsumersSimulator();

        Thread userProducerThread = new Thread(new AddUserRunnable(simulator.queue, simulator.maxUsers,
                simulator.timeIntervalToAddUserInSec), "UserProducerThread");
        userProducerThread.setDaemon(true);

        Thread agentOneThread = new Thread(new HandleUserReqRunnable(simulator.queue,
                simulator.timeIntervalToHanldeUserReqInSec, simulator.countDownLatch), "Agent1Thread");
        agentOneThread.setDaemon(true);

        Thread agentTwoThread = new Thread(new HandleUserReqRunnable(simulator.queue,
                simulator.timeIntervalToHanldeUserReqInSec, simulator.countDownLatch), "Agent2Thread");
        agentTwoThread.setDaemon(true);

        userProducerThread.start();
        agentOneThread.start();
        agentTwoThread.start();

        try {
            simulator.countDownLatch.await(); // block main thread until all workers are done handling users
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        System.out.println("\n" + Thread.currentThread().getName() + " -> All the user requests are handled!!");
    }
}
