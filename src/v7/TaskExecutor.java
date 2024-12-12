package v7;

import java.util.concurrent.*;

public class TaskExecutor {
    private final BlockingQueue<TaskMessage> taskQueue; // Buffer de tarefas
    private final int maxConsumers; // Limite de threads consumidores
    private final ExecutorService consumerPool; // Pool de threads consumidores

    public TaskExecutor(int bufferSize, int maxConsumers) {
        this.taskQueue = new ArrayBlockingQueue<>(bufferSize);
        this.maxConsumers = maxConsumers;
        this.consumerPool = Executors.newFixedThreadPool(maxConsumers);
    }

    // Método para adicionar tarefas
    public void putTask(TaskMessage task) throws InterruptedException {
        taskQueue.put(task);
        System.out.println("Task added to queue: " + task.getDescription());
        startConsumerIfNecessary();
    }

    // Método para gerenciar consumidores
    private void startConsumerIfNecessary() {
        if (((ThreadPoolExecutor) consumerPool).getActiveCount() < maxConsumers) {
            consumerPool.execute(this::consumeTasks);
        }
    }

    // Método que define o comportamento dos consumidores
    private void consumeTasks() {
        try {
            while (true) {
                TaskMessage task = taskQueue.poll(3, TimeUnit.SECONDS); // Timeout de 3s
                if (task == null) {
                    System.out.println("No tasks, consumer shutting down.");
                    break;
                }
                System.out.println("Executing task: " + task.getDescription());
                task.execute();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Método para encerrar o executor
    public void shutdown() {
        consumerPool.shutdown();
        try {
            if (!consumerPool.awaitTermination(5, TimeUnit.SECONDS)) {
                consumerPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            consumerPool.shutdownNow();
        }
    }
}