package v6;

public class Producer extends Thread {
    private final IProdConsBuffer buffer;
    private final int minMessages; 
    private final int maxMessages; 
    private final int prodTime; 
    private final int producerId; 
    private final int consumersRequired; 

    public Producer(IProdConsBuffer buffer, int minMessages, int maxMessages, int prodTime, int consumersRequired) {
        this.buffer = buffer;
        this.minMessages = minMessages;
        this.maxMessages = maxMessages;
        this.prodTime = prodTime;
        this.producerId = (int) this.getId();
        this.consumersRequired = consumersRequired; 
    }

    @Override
    public void run() {
        try {
            buffer.registerProducer(); 
            System.out.println("Producer " + producerId + " started.");
            int numMessages = minMessages + (int) (Math.random() * (maxMessages - minMessages + 1)); 

            for (int i = 0; i < numMessages; i++) {
                Message msg = new Message("message " + i + " of producer " + producerId, producerId, consumersRequired); 
                buffer.put(msg, consumersRequired);
                System.out.println("Producer " + producerId + " produced: " + msg.getContent() + " for " + consumersRequired + " consumers.");
                Thread.sleep(prodTime); 
            }

            System.out.println("Producer " + producerId + " finished.");
        } catch (InterruptedException e) {
            System.err.println("Producer " + producerId + " interrupted.");
        } finally {
            buffer.producerFinished(); 
        }
    }
}