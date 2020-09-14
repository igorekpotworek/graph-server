package igorekpotworek.infrastructure.server.lifecycle;

import igorekpotworek.infrastructure.server.Request;

import java.util.function.Predicate;

public interface CloseConnectionPredicate extends Predicate<Request> {
}
