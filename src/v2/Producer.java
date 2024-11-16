package v2;

public class Producer extends Thread {
    private final IProdConsBuffer buffer;
    private final int minMessages;
    private final int maxMessages;
    private final int prodTime;
    private final int producerId;

    public Producer(IProdConsBuffer buffer, int minMessages, int maxMessages, int prodTime) {
        this.buffer = buffer;
        this.minMessages = minMessages;
        this.maxMessages = maxMessages;
        this.prodTime = prodTime;
        this.producerId = (int) this.getId();
    }

    @Override
    public void run() {
        try {
            buffer.registerProducer(); //notifies the buffer that a producer has started
            System.out.println("Producer " + producerId + " started.");
            int numMessages = minMessages + (int) (Math.random() * (maxMessages - minMessages + 1));
            for (int i = 0; i < numMessages; i++) {
                Message msg = new Message("message " + i + " of producer " + producerId, producerId);
                buffer.put(msg);
                System.out.println("Producer " + producerId + " produced: " + msg.getContent());
                Thread.sleep(prodTime);
            }
            System.out.println("Producer " + producerId + " finished.");
        } catch (InterruptedException e) {
            System.err.println("Producer " + producerId + " interrupted.");
        } finally {
            buffer.producerFinished(); //notifies the buffer that the producer has finished
        }
    }
}
