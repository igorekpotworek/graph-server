package igorekpotworek.infrastructure.server;

import igorekpotworek.infrastructure.server.error.ExceptionHandler;
import igorekpotworek.infrastructure.server.error.RouteNoteFoundException;
import igorekpotworek.infrastructure.server.lifecycle.CloseConnectionPredicate;
import io.vavr.API;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;

import static io.vavr.Predicates.not;
import static io.vavr.control.Option.none;
import static java.util.function.Function.identity;

@RequiredArgsConstructor
class Router {

  private final List<Controller> controllers;
  private final ExceptionHandler exceptionHandler;
  private final CloseConnectionPredicate closeConnectionPredicate;

  void handleRequest(BufferedReader in, PrintWriter out, Session session) {
    Stream.continually(() -> tryRead(in))
        .takeWhile(Option::isDefined)
        .flatMap(identity())
        .map(Request::new)
        .takeWhile(not(closeConnectionPredicate))
        .map(r -> handleRequest(r, session))
        .forEach(it -> out.println(it.body()));
  }

  private Option<String> tryRead(BufferedReader in) {
    return Try.of(in::readLine)
        .map(API::Option)
        .recover(SocketTimeoutException.class, (__) -> none())
        .get();
  }

  private Response handleRequest(Request request, Session session) {
    return controllers
        .find(it -> it.matches(request.getBody()))
        .toTry(RouteNoteFoundException::new)
        .flatMap(it -> it.handle(request, session))
        .getOrElseGet(exceptionHandler::handle);
  }
}
