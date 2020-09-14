package igorekpotworek.graph.controller;

import igorekpotworek.graph.controller.request.ShortestPathRequest;
import igorekpotworek.graph.error.DeserializationException;
import igorekpotworek.graph.repository.GraphRepository;
import igorekpotworek.infrastructure.Deserializer;
import igorekpotworek.infrastructure.server.Controller;
import igorekpotworek.infrastructure.server.Request;
import igorekpotworek.infrastructure.server.Response;
import igorekpotworek.infrastructure.server.Session;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.regex.Pattern;

import static igorekpotworek.infrastructure.server.Response.ok;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;

@RequiredArgsConstructor
public class ShortestPathController implements Controller {

  private static final Pattern PATTERN =
      Pattern.compile("SHORTEST PATH ([a-zA-Z\\-0-9]*) ([a-zA-Z\\-0-9]*)");

  private final GraphRepository graphRepository;
  private final ShortestPathDeserializer deserializer = new ShortestPathDeserializer();

  @Override
  public boolean matches(String command) {
    return PATTERN.matcher(command).matches();
  }

  @Override
  public Try<Response> handle(Request request, Session session) {
    val shortestPathRequest = deserializer.deserialize(request.getBody());
    return shortestPathRequest.flatMap(
        r -> graphRepository.shortestPath(r.getSource(), r.getTarget()).map(p -> ok(p.toString())));
  }

  private static class ShortestPathDeserializer implements Deserializer<ShortestPathRequest> {
    @Override
    public Try<ShortestPathRequest> deserialize(String request) {
      val matcher = PATTERN.matcher(request);
      if (matcher.find()) {
        val source = matcher.group(1);
        val target = matcher.group(2);
        return success(new ShortestPathRequest(source, target));
      } else {
        return failure(new DeserializationException());
      }
    }
  }
}
