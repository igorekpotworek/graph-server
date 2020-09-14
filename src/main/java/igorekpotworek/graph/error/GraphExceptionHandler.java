package igorekpotworek.graph.error;

import igorekpotworek.infrastructure.server.Response;
import igorekpotworek.infrastructure.server.error.ExceptionHandler;
import igorekpotworek.infrastructure.server.error.RouteNoteFoundException;
import lombok.extern.slf4j.Slf4j;

import static igorekpotworek.infrastructure.server.Response.error;
import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

@Slf4j
public class GraphExceptionHandler implements ExceptionHandler {

    public Response handle(Throwable e) {
        return Match(e)
                .of(
                        Case(
                                $(instanceOf(NodeAlreadyExistException.class)),
                                __ -> error("ERROR: NODE ALREADY EXISTS")),
                        Case($(instanceOf(NodeNotFoundException.class)), __ -> error("ERROR: NODE NOT FOUND")),
                        Case(
                                $(instanceOf(RouteNoteFoundException.class)),
                                __ -> new Response.ErrorResponse("SORRY, I DID NOT UNDERSTAND THAT")),
                        Case(
                                $(),
                                __ -> {
                                    log.error("Unexpected error:", e);
                                    return error("ERROR: INTERNAL SERVER ERROR");
                                }));
    }
}
