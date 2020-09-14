package igorekpotworek.graph.controller.request;

import lombok.Value;

@Value
public class RemoveEdgeRequest {
  String source;
  String target;
}
