package v2;

public class Message {
  private String content;  
  private int producerId; 

  public Message(String content, int producerId) {
      this.content = content;
      this.producerId = producerId;
  }

  public String getContent() {
      return content;
  }

  public int getProducerId() {
      return producerId;
  }
}
