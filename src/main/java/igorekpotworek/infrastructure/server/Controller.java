package igorekpotworek.infrastructure.server;

import io.vavr.control.Try;

public interface Controller {

  boolean matches(String command);

  Try<Response> handle(Request request, Session session);
}
