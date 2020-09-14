package igorekpotworek.infrastructure;

import io.vavr.control.Try;

public interface Deserializer<T> {
    Try<T> deserialize(String request);
}
