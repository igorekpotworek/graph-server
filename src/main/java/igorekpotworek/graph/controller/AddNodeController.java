package igorekpotworek.graph.controller;

import igorekpotworek.graph.controller.request.AddNodeRequest;
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
public class AddNodeController implements Controller {

    private static final Pattern PATTERN = Pattern.compile("ADD NODE ([a-zA-Z\\-0-9]*)");

    private final GraphRepository graphRepository;
    private final AddNodeDeserializer deserializer = new AddNodeDeserializer();

    @Override
    public boolean matches(String command) {
        return PATTERN.matcher(command).matches();
    }

    @Override
    public Try<Response> handle(Request request, Session session) {
        val addNodeRequest = deserializer.deserialize(request.getBody());
        return addNodeRequest
                .flatMap(r -> graphRepository.addNode(r.getName()))
                .map(__ -> ok("NODE ADDED"));
    }

    private static class AddNodeDeserializer implements Deserializer<AddNodeRequest> {
        @Override
        public Try<AddNodeRequest> deserialize(String request) {
            val matcher = PATTERN.matcher(request);
            if (matcher.find()) {
                return success(new AddNodeRequest(matcher.group(1)));
            } else {
                return failure(new DeserializationException());
            }
        }
    }
}
