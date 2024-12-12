package v7;

public class Producer extends Thread {
    private final TaskExecutor taskExecutor; // Gerenciador de tarefas
    private final int numTasks; // Número de tarefas a produzir
    private final int prodTime; // Tempo entre produções

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

                // Criando a tarefa como um Runnable
                Runnable task = () -> {
                    System.out.println("Executing: " + taskDescription);
                };

                // Adicionando a tarefa ao executor
                TaskMessage taskMessage = new TaskMessage(task, taskDescription);
                taskExecutor.putTask(taskMessage);

                Thread.sleep(prodTime); // Simula o tempo de produção
            }
            System.out.println("Producer " + this.getId() + " finished.");
        } catch (InterruptedException e) {
            System.err.println("Producer " + this.getId() + " interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}