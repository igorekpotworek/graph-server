package igorekpotworek.graph.model;

import igorekpotworek.graph.error.NodeAlreadyExistException;
import igorekpotworek.graph.error.NodeNotFoundException;
import io.vavr.collection.*;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.val;

import static io.vavr.API.For;
import static io.vavr.control.Try.failure;
import static io.vavr.control.Try.success;

public final class DirectedGraph<N> implements Graph<N> {

  private final Map<N, Node<N>> nodes;

  private DirectedGraph(Map<N, Node<N>> nodes) {
    this.nodes = nodes;
  }

  public DirectedGraph() {
    this(HashMap.empty());
  }

  public DirectedGraph(Seq<Node<N>> nodes) {
    this(nodes.groupBy(Node::getName).mapValues(Traversable::head));
  }

  @Override
  public Try<Graph<N>> addNode(N name) {
    val node = new Node<>(name);
    if (!containsNode(name)) {
      return success(new DirectedGraph<>(nodes.put(name, node)));
    } else {
      return failure(new NodeAlreadyExistException());
    }
  }

  @Override
  public Try<Graph<N>> removeNode(N name) {
    if (containsNode(name)) {
      Seq<Node<N>> updatedNodes = nodes.remove(name).values().map((n) -> n.removeEdge(n));
      return success(new DirectedGraph<>(updatedNodes));
    } else {
      return failure(new NodeNotFoundException());
    }
  }

  @Override
  public boolean containsNode(N name) {
    return nodes.containsKey(name);
  }

  private Graph<N> replaceNode(Node<N> newNode) {
    return new DirectedGraph<>(nodes.put(newNode.getName(), newNode));
  }

  @Override
  public Set<Node<N>> neighbours(Node<N> node) {
    return node.neighbours().flatMap(this::getNode);
  }

  @Override
  public Try<Graph<N>> removeEdge(N source, N target) {
    return For(getNode(source), getNode(target))
        .yield((s, t) -> replaceNode(s.removeEdge(t)))
        .toTry(NodeNotFoundException::new);
  }

  @Override
  public Option<Node<N>> getNode(N name) {
    return nodes.get(name);
  }

  @Override
  public Try<Graph<N>> addEdge(N source, N target, int weight) {
    return For(getNode(source), getNode(target))
        .yield((s, t) -> replaceNode(s.addEdge(t, weight)))
        .toTry(NodeNotFoundException::new);
  }

  @Override
  public DirectedGraph<N> removeAllVertices() {
    return new DirectedGraph<>();
  }

  @Override
  public boolean containsEdge(N source, N target) {
    return getNode(source).map(it -> it.isNeighbour(target)).getOrElse(false);
  }
}
