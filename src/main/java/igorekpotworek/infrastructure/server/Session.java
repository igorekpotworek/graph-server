package igorekpotworek.infrastructure.server;

import io.vavr.control.Option;
import lombok.Data;

import java.util.UUID;

import static io.vavr.control.Option.none;
import static io.vavr.control.Option.some;
import static java.lang.System.currentTimeMillis;
import static java.util.UUID.randomUUID;

@Data
public class Session {

  private final UUID id;
  private final long timestamp;

  private Option<String> clientName = none();

  public Session(UUID id, long timestamp) {
    this.id = id;
    this.timestamp = timestamp;
  }

  public static Session getInstance() {
    return new Session(randomUUID(), currentTimeMillis());
  }

  public String getClientName() {
    return clientName.getOrElse("ANONYMOUS");
  }

  public void setClientName(String clientName) {
    this.clientName = some(clientName);
  }

  public long sessionTime() {
    return currentTimeMillis() - timestamp;
  }
}
