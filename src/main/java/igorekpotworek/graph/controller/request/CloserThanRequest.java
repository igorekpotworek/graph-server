package igorekpotworek.graph.controller.request;

import lombok.Value;

@Value
public class CloserThanRequest {
    String source;
    int radius;
}
