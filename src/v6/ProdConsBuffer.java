package v6;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProdConsBuffer implements IProdConsBuffer {
    private final Message[] buffer;
    private int nfull = 0;
    private int tail = 0;
    private int head = 0;
    private int totalMessages = 0;
    private int activeProducers;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public ProdConsBuffer(int bufSize) {
        buffer = new Message[bufSize];
    }

    @Override
    public void put(Message m, int n) throws InterruptedException {
        lock.lock();
        try {
            while (nfull == buffer.length) {
                notFull.await();
            }

            m = new Message(m.getContent(), m.getProducerId(), n);
            buffer[tail] = m;
            tail = (tail + 1) % buffer.length;
            nfull++;
            totalMessages++;
            System.out.println("Message added to buffer: " + m.getContent() + " for " + n + " consumers.");
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Message get() throws InterruptedException {
        lock.lock();
        try {
            while (nfull == 0 && hasActiveProducers()) {
                notEmpty.await();
            }

            if (nfull == 0 && !hasActiveProducers()) {
                return null;
            }

            Message message = buffer[head];
            message.consume();

            if (message.isFullyConsumed()) {
                System.out.println("Mensagem totalmente consumida e removida: " + message.getContent());
                head = (head + 1) % buffer.length;
                nfull--;
                notFull.signalAll();
            }

            return message;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int nmsg() {
        lock.lock();
        try {
            return nfull;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int totmsg() {
        lock.lock();
        try {
            return totalMessages;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void registerProducer() {
        lock.lock();
        try {
            activeProducers++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void producerFinished() {
        lock.lock();
        try {
            activeProducers--;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public boolean hasActiveProducers() {
        lock.lock();
        try {
            return activeProducers > 0;
        } finally {
            lock.unlock();
        }
    }
}