package igorekpotworek.graph.controller;

import igorekpotworek.graph.controller.request.CloserThanRequest;
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
public class CloserThanController implements Controller {

  private static final Pattern PATTERN = Pattern.compile("CLOSER THAN (\\d+) ([a-zA-Z\\-0-9]*)");

  private final GraphRepository graphRepository;
  private final CloserThanDeserializer deserializer = new CloserThanDeserializer();

  @Override
  public boolean matches(String command) {
    return PATTERN.matcher(command).matches();
  }

  @Override
  public Try<Response> handle(Request request, Session session) {
    val closerThanRequest = deserializer.deserialize(request.getBody());
    return closerThanRequest
        .flatMap(r -> graphRepository.closerThan(r.getSource(), r.getRadius()))
        .map(n -> ok(n.toSortedSet().mkString(",")));
  }

  private static class CloserThanDeserializer implements Deserializer<CloserThanRequest> {

    @Override
    public Try<CloserThanRequest> deserialize(String request) {
      val matcher = PATTERN.matcher(request);
      if (matcher.find()) {
        val weight = Integer.parseInt(matcher.group(1));
        val source = matcher.group(2);
        return success(new CloserThanRequest(source, weight));
      } else {
        return failure(new DeserializationException());
      }
    }
  }
}
