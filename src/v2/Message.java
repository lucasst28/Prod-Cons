package v2;

public class Message {
  private String content;  // Conteúdo da mensagem
  private int producerId;  // ID do produtor que gerou a mensagem

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
