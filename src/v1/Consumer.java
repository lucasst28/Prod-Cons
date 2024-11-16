package v1;

public class Consumer extends Thread {
    private final IProdConsBuffer buffer;
    private final int consTime;
    private final int consumerId;

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
                System.out.println(" Consumer " + consumerId + " consumed: " + msg.getContent());
                Thread.sleep(consTime);
            }
        } catch (InterruptedException e) {
            System.err.println("Consumer " + consumerId + " interrupted.");
        }
    }
}