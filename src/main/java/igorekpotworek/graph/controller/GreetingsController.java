package igorekpotworek.graph.controller;

import igorekpotworek.graph.controller.request.GreetingsRequest;
import igorekpotworek.graph.error.DeserializationException;
import igorekpotworek.infrastructure.Deserializer;
import igorekpotworek.infrastructure.server.Controller;
import igorekpotworek.infrastructure.server.Request;
import igorekpotworek.infrastructure.server.Response;
import igorekpotworek.infrastructure.server.Session;
import io.vavr.control.Try;
import lombok.val;

import java.util.regex.Pattern;

import static igorekpotworek.infrastructure.server.Response.ok;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;

public class GreetingsController implements Controller {

  private static final Pattern PATTERN = Pattern.compile("HI, I AM ([a-zA-Z\\-0-9]*)");

  private final GreetingsDeserializer deserializer = new GreetingsDeserializer();

  @Override
  public boolean matches(String command) {
    return PATTERN.matcher(command).matches();
  }

  @Override
  public Try<Response> handle(Request request, Session session) {
    val greetingsRequest = deserializer.deserialize(request.getBody());
    return greetingsRequest.map(
        r -> {
          session.setClientName(r.getName());
          return ok(String.format("HI %s", r.getName()));
        });
  }

  private static class GreetingsDeserializer implements Deserializer<GreetingsRequest> {
    @Override
    public Try<GreetingsRequest> deserialize(String request) {
      val matcher = PATTERN.matcher(request);
      if (matcher.find()) {
        return success(new GreetingsRequest(matcher.group(1)));
      } else {
        return failure(new DeserializationException());
      }
    }
  }
}
