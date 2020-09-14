package igorekpotworek.graph.controller.request;

import lombok.Value;

@Value
public class ShortestPathRequest {
    String source;
    String target;
}
