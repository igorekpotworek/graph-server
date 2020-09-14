package igorekpotworek.graph.model;

import io.vavr.collection.HashMultimap;
import io.vavr.collection.Multimap;
import io.vavr.collection.Set;
import io.vavr.collection.Traversable;
import lombok.Getter;

public final class Node<N> {

    @Getter
    private final N name;
    private final Multimap<N, Integer> neighbours;

    public Node(N name) {
        this.name = name;
        this.neighbours = HashMultimap.withSeq().empty();
    }

    private Node(N name, Multimap<N, Integer> neighbours) {
        this.name = name;
        this.neighbours = neighbours;
    }

    public int weight(Node<N> target) {
        return weight(target.name);
    }

    public int weight(N target) {
        return neighbours.get(target).flatMap(Traversable::min).get();
    }

    public Set<N> neighbours() {
        return neighbours.keySet();
    }

    public Node<N> addEdge(Node<N> target, int weight) {
        return new Node<>(name, neighbours.put(target.name, weight));
    }

    public Node<N> removeEdge(Node<N> target) {
        return new Node<>(name, neighbours.remove(target.getName()));
    }
}
