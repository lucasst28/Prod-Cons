package v6;

public class Message {
    private String content;  
    private int producerId;  
    private int remainingConsumers;

    public Message(String content, int producerId, int totalConsumers) {
        this.content = content;
        this.producerId = producerId;
        this.remainingConsumers = totalConsumers; 
    }

    public String getContent() {
        return content;
    }

    public int getProducerId() {
        return producerId;
    }

    public synchronized void consume() {
        if (remainingConsumers > 0) {
            remainingConsumers--;
            System.out.println("Mensagem consumida. Restam " + remainingConsumers + " consumidores.");
        }
    }

    public synchronized boolean isFullyConsumed() {
        return remainingConsumers == 0;
    }

    public synchronized int getRemainingConsumers() {
        return remainingConsumers;
    }
}