import java.util.*;

/**
 * Program to execute all given tasks with the list of given workers.
 *
 * Program Constraints:
 * Each task needs to be executed in exactly 3 stages: Stage 0, Stage 1 and Stage 2.
 * Each task stage must be done by separate workers, i.e. for a task, same worker cannot do more than one stage.
 * Each task stage takes exactly 1 minute to complete the task.
 *
 * As part of task execution, print output in following format:
 * Time: 0
 * Assigned task T1 to worker W1 for stage 0
 * Assigned task T2 to worker W2 for stage 0
 * Completed task T1 with worker W1 for stage 0
 * Completed task T2 with worker W2 for stage 0
 *
 * Time: 1
 * Assigned task T1 to worker W3 for stage 1
 * Assigned task T2 to worker W3 for stage 1
 * Completed task T1 with worker W3 for stage 1
 * Completed task T2 with worker W3 for stage 1
 *
 * Time: 2
 * Assigned task T1 to worker W4 for stage 2
 * Assigned task T2 to worker W1 for stage 2
 * Completed task T1 with worker W4 for stage 2
 * Completed task T2 with worker W1 for stage 2
 *
 * Total time taken to complete all tasks in minutes: 3
 *
 * @author Prabhash Rathore
 */
public class TaskWorkerDelegationSimulator {

    /**
     * Class to encapsulate Task data.
     */
    public static class Task {
        private String name;

        // last stage done for this task
        private int stageCompleted;

        // efficient way to keep track of workers who has worked on this task
        private Set<Worker> workersSet;

        public Task(String name) {
            this.name = name;
            this.stageCompleted = -1;
            this.workersSet = new HashSet<>();
        }
    }

    /**
     * Class to encapsulate worker name.
     */
    public static class Worker {
        private String name;

        public Worker(String name) {
            this.name = name;
        }
    }

    /**
     * This is the main algorithm where tasks are delegated to workers based on constraints provided in problem
     * statement and prints the task execution order.
     *
     * Task is stored in queue and after each execution of task stage, it's pushed back to same queue. When for a task,
     * all it's 3 stages are completed, task is ejected out of the queue.
     *
     * @param tasks
     * @param workers
     */
    public static void executeTasks(List<Task> tasks, List<Worker> workers) {

        Queue<Task> queue = new LinkedList<>();
        for (Task task : tasks) {
            queue.add(task);
        }

        int workerCount = workers.size();

        int timeInMinute = 0;

        int windowSize = Math.min(tasks.size(), workers.size());
        int curCount = 0;

        int workerIndex = 0;

        List<String> completedStages = new ArrayList<>();

        while (!queue.isEmpty()) {

            curCount++;

            Task task = queue.remove();

            // Task with stage == 2 is marked as done
            if (task.stageCompleted == 2) {
                continue;
            }

            if (curCount == 1) {
                System.out.println("\nTime: " + timeInMinute);
            }

            int curStage = task.stageCompleted + 1;

            workerIndex = getNextValidWorker(task, workers, workerIndex);
            Worker worker = workers.get(workerIndex);

            System.out.println("Assigned task " + task.name + " to worker " + worker.name + " for stage " + curStage);

            task.stageCompleted = curStage;
            task.workersSet.add(worker);

            queue.add(task); // add it back to queue for next stage processing

            completedStages.add(String.format("Completed task %s with worker %s for stage %d", task.name, worker.name, curStage));

            workerIndex = (workerIndex + 1) % workerCount;

            if (curCount == windowSize) {
                // print completed messages
                for (String s : completedStages) {
                    System.out.println(s);
                }

                completedStages.clear();
                curCount = 0;
                timeInMinute++;
            }
        }

        System.out.println("\n\nTotal time taken to complete all tasks in minutes: " + timeInMinute);

    }

    private static int getNextValidWorker(Task task, List<Worker> workers, int index) {

        int size = workers.size();
        int count = 0;

        while (count < size) {
            Worker worker = workers.get(index);

            if (!task.workersSet.contains(worker)) {
                return index;
            }

            index = (index + 1) % size;
            count++;
        }

        // this must not happen based on valid inputs
        throw new RuntimeException("Not possible to assign workers in this case");
    }

    public static void main(String args[]) {

        List<Task> tasks = List.of(new Task("T1"), new Task("T2"), new Task("T3"));
        List<Worker> workers = List.of(new Worker("W1"), new Worker("W2"), new Worker("W3"), new Worker("W3"), new Worker("W4"));

        executeTasks(tasks, workers);
    }
}