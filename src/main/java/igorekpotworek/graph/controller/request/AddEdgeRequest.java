package igorekpotworek.graph.controller.request;

import lombok.Value;

@Value
public class AddEdgeRequest {
    String source;
    String target;
    int weight;
}
