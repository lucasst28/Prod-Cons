package v6;

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
                Message message = buffer.get(); // Consome uma mensagem por vez
                if (message == null) {
                    break; // Sai se não há mais mensagens e produtores acabaram
                }
                System.out.println("Consumer " + consumerId + " consumed: " + message.getContent());
                Thread.sleep(consTime); // Simula tempo de processamento
            }
            System.out.println("Consumer " + consumerId + " finished.");
        } catch (InterruptedException e) {
            System.err.println("Consumer " + consumerId + " interrupted.");
        }
    }
}