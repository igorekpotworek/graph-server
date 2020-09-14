package igorekpotworek.graph.algortihms;

import igorekpotworek.graph.error.NodeNotFoundException;
import igorekpotworek.graph.model.Graph;
import igorekpotworek.graph.model.Node;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.*;
import io.vavr.control.Try;
import lombok.val;

import static io.vavr.API.For;
import static java.lang.Integer.MAX_VALUE;
import static java.util.Comparator.comparing;

public class DijkstraAlgorithm<N> implements ShortestPath<N>, CloserThan<N> {

  private final Graph<N> graph;

  public DijkstraAlgorithm(Graph<N> graph) {
    this.graph = graph;
  }

  private Map<Node<N>, Integer> calculateDistances(Node<N> source) {
    Set<Node<N>> settledNodes = HashSet.empty();
    PriorityQueue<Tuple2<Node<N>, Integer>> unSettledNodes =
        PriorityQueue.of(comparing(Tuple2::_2), Tuple.of(source, 0));
    Map<Node<N>, Integer> distances = HashMap.of(source, 0);

    while (!unSettledNodes.isEmpty()) {
      val dequeue = unSettledNodes.dequeue();
      val node = dequeue._1._1;
      unSettledNodes = dequeue._2;
      val neighbours = graph.neighbours(node);

      for (val target : neighbours) {
        if (!settledNodes.contains(target)) {
          val distance = distances.getOrElse(node, MAX_VALUE) + node.weight(target);
          if (distances.getOrElse(target, MAX_VALUE) > distance) {
            distances = distances.put(target, distance);
            unSettledNodes = unSettledNodes.enqueue(Tuple.of(target, distance));
          }
        }
      }
      settledNodes = settledNodes.add(node);
    }
    return distances;
  }

  public Try<Set<N>> closerThan(N source, int radius) {
    return graph
        .getNode(source)
        .map((s) -> closerThanFunc(s, radius))
        .toTry(NodeNotFoundException::new);
  }

  private Set<N> closerThanFunc(Node<N> source, int radius) {
    return calculateDistances(source)
        .filterValues(it -> it < radius)
        .keySet()
        .filter(it -> !it.equals(source))
        .map(Node::getName);
  }

  public Try<Integer> shortestPath(N source, N target) {
    return For(graph.getNode(source), graph.getNode(target))
        .yield((s, t) -> calculateDistances(s).getOrElse(t, MAX_VALUE))
        .toTry(NodeNotFoundException::new);
  }
}
