package igorekpotworek.infrastructure.server;

import lombok.Data;

@Data
public class ServerConfig {
  private int port;
  private int numberOfThreads;
  private int socketTimeout;
}
