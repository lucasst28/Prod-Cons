package v7;

public class TaskMessage {
  private final Runnable task; // A tarefa a ser executada
  private final String description; // Uma descrição opcional da tarefa

  public TaskMessage(Runnable task, String description) {
      this.task = task;
      this.description = description;
  }

  // Método para executar a tarefa
  public void execute() {
      task.run();
  }

  public String getDescription() {
      return description;
  }
}