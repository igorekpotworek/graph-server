package igorekpotworek.graph.repository;

import igorekpotworek.graph.algortihms.DijkstraAlgorithm;
import igorekpotworek.graph.model.Graph;
import igorekpotworek.infrastructure.Guard;
import io.vavr.collection.Set;
import io.vavr.control.Try;
import lombok.val;

public class GraphRepository {
    private final Guard guard = new Guard();
    private Graph<String> graph = new Graph<>();

    public Try<Graph<String>> addNode(String node) {
        return guard.write(() -> graph.addNode(node).onSuccess(g -> graph = g));
    }

    public Try<Graph<String>> removeNode(String node) {
        return guard.write(() -> graph.removeNode(node).onSuccess(g -> graph = g));
    }

    public Try<Graph<String>> addEdge(String source, String target, int weight) {
        return guard.write(() -> graph.addEdge(source, target, weight).onSuccess(g -> graph = g));
    }

    public Try<Graph<String>> removeEdge(String source, String target) {
        return guard.write(() -> graph.removeEdge(source, target).onSuccess(g -> graph = g));
    }

    public Try<Integer> shortestPath(String source, String target) {
        return guard.read(
                () -> {
                    val algo = new DijkstraAlgorithm<>(graph);
                    return algo.shortestPath(source, target);
                });
    }

    public Try<Set<String>> closerThan(String source, int radius) {
        return guard.read(
                () -> {
                    val algo = new DijkstraAlgorithm<>(graph);
                    return algo.closerThan(source, radius);
                });
    }

    public boolean containsNode(String node) {
        return guard.read(() -> graph.containsNode(node));
    }

    public boolean containsEdge(String source, String target) {
        return guard.read(() -> graph.containsEdge(source, target));
    }

    public Graph<String> removeAllVertices() {
        return guard.write(
                () -> {
                    graph = graph.removeAllVertices();
                    return graph;
                });
    }
}
