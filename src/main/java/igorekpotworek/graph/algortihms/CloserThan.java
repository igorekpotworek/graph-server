package igorekpotworek.graph.algortihms;

import io.vavr.collection.Set;
import io.vavr.control.Try;

public interface CloserThan<N> {
    Try<Set<N>> closerThan(N source, int radius);
}
