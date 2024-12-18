/**
 * Simulation of a Producer with multiple consumers.
 *
 * A single producer adds users with unique id to a call handling queue every 2 seconds. While there are
 * 2 agents who handle these added users from queue. Each agent takes 4 seconds to handle each user's request. There
 * are a total of 6 users to be handled.
 *
 * Produce a simulation of messages saying - User added, User handled per user and all users done when simulation
 * completes.
 *
 * @author Prabhash Rathore
 *
 */
public class ProducerWithMultipleConsumersSimulator {

    public static void main(String[] args) {
        System.out.println("Simulation of a Producer with multiple consumers.");
    }
}
