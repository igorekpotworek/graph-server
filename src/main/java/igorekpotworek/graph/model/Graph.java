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

public final class Graph<N> {

    private final Map<N, Node<N>> nodes;

    private Graph(Map<N, Node<N>> nodes) {
        this.nodes = nodes;
    }

    public Graph() {
        this(HashMap.empty());
    }

    public Graph(Seq<Node<N>> nodes) {
        this(nodes.groupBy(Node::getName).mapValues(Traversable::head));
    }

    public Try<Graph<N>> addNode(N name) {
        val node = new Node<>(name);
        if (!containsNode(name)) {
            return success(new Graph<>(nodes.put(name, node)));
        } else {
            return failure(new NodeAlreadyExistException());
        }
    }

    public Try<Graph<N>> removeNode(N name) {
        if (containsNode(name)) {
            Seq<Node<N>> updatedNodes = nodes.remove(name).values().map((n) -> n.removeEdge(n));
            return success(new Graph<>(updatedNodes));
        } else {
            return failure(new NodeNotFoundException());
        }
    }

    public boolean containsNode(N name) {
        return nodes.containsKey(name);
    }

    private Graph<N> replaceNode(Node<N> oldNode, Node<N> newNode) {
        return new Graph<>(nodes.values().replace(oldNode, newNode));
    }

    public Set<Node<N>> neighbours(Node<N> node) {
        return node.neighbours().flatMap(this::getNode);
    }

    public Try<Graph<N>> removeEdge(N source, N target) {
        return For(getNode(source), getNode(target))
                .yield((s, t) -> replaceNode(s, s.removeEdge(t)))
                .toTry(NodeNotFoundException::new);
    }

    public Option<Node<N>> getNode(N name) {
        return nodes.get(name);
    }

    public Try<Graph<N>> addEdge(N source, N target, int weight) {
        return For(getNode(source), getNode(target))
                .yield((s, t) -> replaceNode(s, s.addEdge(t, weight)))
                .toTry(NodeNotFoundException::new);
    }

    public Graph<N> removeAllVertices() {
        return new Graph<>();
    }

    public boolean containsEdge(N source, N target) {
        return getNode(source).map(it -> it.neighbours().contains(target)).getOrElse(false);
    }
}
