package v7;

public class Producer extends Thread {
    private final TaskExecutor taskExecutor; 
    private final int numTasks; 
    private final int prodTime; 

    public Producer(TaskExecutor taskExecutor, int numTasks, int prodTime) {
        this.taskExecutor = taskExecutor;
        this.numTasks = numTasks;
        this.prodTime = prodTime;
    }

    @Override
    public void run() {
        try {
            System.out.println("Producer " + this.getId() + " started.");
            for (int i = 0; i < numTasks; i++) {
                String taskDescription = "Task " + i + " from producer " + this.getId();

                Runnable task = () -> {
                    System.out.println("Executing: " + taskDescription);
                };

                TaskMessage taskMessage = new TaskMessage(task, taskDescription);
                taskExecutor.putTask(taskMessage);

                Thread.sleep(prodTime); 
            }
            System.out.println("Producer " + this.getId() + " finished.");
        } catch (InterruptedException e) {
            System.err.println("Producer " + this.getId() + " interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}