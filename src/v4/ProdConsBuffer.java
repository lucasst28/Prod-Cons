package v4;

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
        activeProducers = 0; 
    }

    public void registerProducer() {
        lock.lock();
        try {
            activeProducers++;
        } finally {
            lock.unlock();
        }
    }

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

    @Override
    public void put(Message m) throws InterruptedException {
        lock.lock();
        try {
            while (nfull == buffer.length) {
                notFull.await(); 
            }
            buffer[tail] = m;
            tail = (tail + 1) % buffer.length;
            nfull++;
            totalMessages++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Message get() throws InterruptedException {
        lock.lock();
        try {
            while (nfull == 0 && hasActiveProducers()) {
                notEmpty.await(); //wait while the buffer is empty and there are active producers
            }
            if (nfull == 0 && !hasActiveProducers()) {
                return null; //if the buffer is empty and there are no active producers, the consumer should finish
            }
            Message message = buffer[head];
            head = (head + 1) % buffer.length;
            nfull--;
            notFull.signal();
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
}