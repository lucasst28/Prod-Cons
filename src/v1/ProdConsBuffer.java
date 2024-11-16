package v1;

public class ProdConsBuffer implements IProdConsBuffer {
  private Message[] buffer; 
  private int nfull = 0; 
  private int tail = 0; //entry index
  private int head = 0; //exit index
  private int totalMessages = 0; 

  public ProdConsBuffer(int bufSize) {
      buffer = new Message[bufSize];
  }

  @Override
  public synchronized void put(Message m) throws InterruptedException {
      while (nfull == buffer.length) {
          System.out.println("Buffer full. Producer waiting...");
          wait(); 
      }
      buffer[tail] = m; 
      tail = (tail + 1) % buffer.length; 
      nfull++;
      totalMessages++;
      System.out.println("Message added to buffer: " + m.getContent() + " | Buffer now has " + nfull + " messages.");
      notifyAll();
  }
  
  @Override
  public synchronized Message get() throws InterruptedException {
      while (nfull == 0) {
          System.out.println("Buffer empty. Consumer waiting...");
          wait(); 
      }
      Message message = buffer[head]; 
      head = (head + 1) % buffer.length; 
      nfull--;
      System.out.println("Message consumed in buffer: " + message.getContent() + " | Buffer now has " + nfull + " messages.");
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
