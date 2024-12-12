package v5;

public class Consumer extends Thread {
    private final IProdConsBuffer buffer;
    private final int consTime;
    private int consumerId;

    public Consumer(IProdConsBuffer buffer, int consTime) {
        this.buffer = buffer;
        this.consTime = consTime;
        this.consumerId = (int) this.getId();
    }

    @Override
    public void run() {
        try {
            System.out.println("Consumer " + consumerId + " started.");
            while (true) {
                Message msg = buffer.get();
                if (msg == null) {
                    break; //if the buffer is empty and there are no active producers, the consumer should finish
                }
                System.out.println("Consumer " + consumerId + " consumed: " + msg.getContent());
                Thread.sleep(consTime);
            }
            System.out.println("Consumer " + consumerId + " finished.");
        } catch (InterruptedException e) {
            System.err.println("Consumer " + consumerId + " interrupted.");
        }
    }
}