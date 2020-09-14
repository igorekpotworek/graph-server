package igorekpotworek.graph.algortihms;

import io.vavr.control.Try;

public interface ShortestPath<N> {
    Try<Integer> shortestPath(N source, N target);
}
