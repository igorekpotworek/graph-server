package igorekpotworek.infrastructure.server.error;

import igorekpotworek.infrastructure.server.Response;

public interface ExceptionHandler {
    Response handle(Throwable e);
}
