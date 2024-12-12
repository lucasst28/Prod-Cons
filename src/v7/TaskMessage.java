package v7;

public class TaskMessage {
  private final Runnable task; 
  private final String description; 

  public TaskMessage(Runnable task, String description) {
      this.task = task;
      this.description = description;
  }

  public void execute() {
      task.run();
  }

  public String getDescription() {
      return description;
  }
}