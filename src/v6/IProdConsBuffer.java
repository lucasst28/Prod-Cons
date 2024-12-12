package v6;

public interface IProdConsBuffer {

    /**
     * Put a single message in the buffer.
     */
    //public void put(Message m) throws InterruptedException;

    /**
     * Put a message with 'n' exemplars in the buffer.
     * The producer is blocked until all exemplars are consumed.
     */
    public void put(Message m, int n) throws InterruptedException;

    /**
     * Retrieve a single message from the buffer.
     */
    public Message get() throws InterruptedException;

    /**
     * Retrieve 'k' consecutive messages from the buffer.
     */
    //public Message[] get(int k) throws InterruptedException;

    /**
     * Get the number of messages currently in the buffer.
     */
    public int nmsg();

    /**
     * Get the total number of messages produced.
     */
    public int totmsg();

    /**
     * Register a producer as active.
     */
    public void registerProducer();

    /**
     * Mark a producer as finished.
     */
    public void producerFinished();
}