package v6;

public class Producer extends Thread {
    private final IProdConsBuffer buffer;
    private final int minMessages; // Número mínimo de mensagens a produzir
    private final int maxMessages; // Número máximo de mensagens a produzir
    private final int prodTime; // Tempo de produção entre mensagens
    private final int producerId; // ID único do produtor
    private final int consumersRequired; // Número de consumidores necessários (definido pelo teste)

    public Producer(IProdConsBuffer buffer, int minMessages, int maxMessages, int prodTime, int consumersRequired) {
        this.buffer = buffer;
        this.minMessages = minMessages;
        this.maxMessages = maxMessages;
        this.prodTime = prodTime;
        this.producerId = (int) this.getId();
        this.consumersRequired = consumersRequired; // Define o número de consumidores necessários
    }

    @Override
    public void run() {
        try {
            buffer.registerProducer(); // Notifica que um produtor foi registrado
            System.out.println("Producer " + producerId + " started.");
            int numMessages = minMessages + (int) (Math.random() * (maxMessages - minMessages + 1)); // Define o número de mensagens a produzir

            for (int i = 0; i < numMessages; i++) {
                Message msg = new Message("message " + i + " of producer " + producerId, producerId, consumersRequired); // Cria mensagem com 'n' consumidores necessários
                buffer.put(msg, consumersRequired); // Insere a mensagem no buffer
                System.out.println("Producer " + producerId + " produced: " + msg.getContent() + " for " + consumersRequired + " consumers.");
                Thread.sleep(prodTime); // Simula o tempo de produção
            }

            System.out.println("Producer " + producerId + " finished.");
        } catch (InterruptedException e) {
            System.err.println("Producer " + producerId + " interrupted.");
        } finally {
            buffer.producerFinished(); // Notifica que este produtor terminou
        }
    }
}