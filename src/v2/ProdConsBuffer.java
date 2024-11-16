package v2;

public class ProdConsBuffer implements IProdConsBuffer {
    private Message[] buffer;
    private int nfull = 0; 
    private int tail = 0;
    private int head = 0; 
    private int totalMessages = 0; 
    private int activeProducers; 

    public ProdConsBuffer(int bufSize) {
        buffer = new Message[bufSize];
        activeProducers = 0; 
    }

    public synchronized void registerProducer() {
        activeProducers++; //when a producer starts, increment the count
    }

    public synchronized void producerFinished() {
        activeProducers--; 
        notifyAll(); 
    }

    public synchronized boolean hasActiveProducers() {
        return activeProducers > 0;
    }

    @Override
    public synchronized void put(Message m) throws InterruptedException {
        while (nfull == buffer.length) {
            wait(); 
        }
        buffer[tail] = m; 
        tail = (tail + 1) % buffer.length; 
        nfull++;
        totalMessages++;
        notifyAll(); 
    }

    @Override
    public synchronized Message get() throws InterruptedException {
        while (nfull == 0 && hasActiveProducers()) {
            wait(); 
        }
        if (nfull == 0 && !hasActiveProducers()) {
            return null; 
        }
        Message message = buffer[head]; 
        head = (head + 1) % buffer.length; 
        nfull--;
        notifyAll();
        return message;
    }

    @Override
    public synchronized int nmsg() {
        return nfull;
    }

    @Override
    public synchronized int totmsg() {
        return totalMessages;
    }
}