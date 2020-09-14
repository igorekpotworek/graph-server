package igorekpotworek.graph.model;

import io.vavr.collection.Set;
import io.vavr.control.Option;
import io.vavr.control.Try;

public interface Graph<N> {

  Try<Graph<N>> addNode(N name);

  Try<Graph<N>> removeNode(N name);

  boolean containsNode(N name);

  Set<Node<N>> neighbours(Node<N> node);

  Try<Graph<N>> removeEdge(N source, N target);

  Option<Node<N>> getNode(N name);

  Try<Graph<N>> addEdge(N source, N target, int weight);

  Graph<N> removeAllVertices();

  boolean containsEdge(N source, N target);
}
