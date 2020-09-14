package igorekpotworek.graph.controller;

import igorekpotworek.graph.controller.request.AddEdgeRequest;
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
public class AddEdgeController implements Controller {

  private static final Pattern PATTERN =
      Pattern.compile("ADD EDGE ([a-zA-Z\\-0-9]*) ([a-zA-Z\\-0-9]*) ([1-9]\\d*)");

  private final GraphRepository graphRepository;
  private final AddEdgeDeserializer deserializer = new AddEdgeDeserializer();

  @Override
  public boolean matches(String command) {
    return PATTERN.matcher(command).matches();
  }

  @Override
  public Try<Response> handle(Request request, Session session) {
    val addEdgeRequest = deserializer.deserialize(request.getBody());
    return addEdgeRequest
        .flatMap(r -> graphRepository.addEdge(r.getSource(), r.getTarget(), r.getWeight()))
        .map(__ -> ok("EDGE ADDED"));
  }

  private static class AddEdgeDeserializer implements Deserializer<AddEdgeRequest> {
    @Override
    public Try<AddEdgeRequest> deserialize(String request) {
      val matcher = PATTERN.matcher(request);
      if (matcher.find()) {
        val source = matcher.group(1);
        val target = matcher.group(2);
        val weight = Integer.parseInt(matcher.group(3));
        return success(new AddEdgeRequest(source, target, weight));
      } else {
        return failure(new DeserializationException());
      }
    }
  }
}
