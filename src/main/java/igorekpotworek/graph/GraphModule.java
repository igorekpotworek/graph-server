package igorekpotworek.graph;

import igorekpotworek.graph.controller.*;
import igorekpotworek.graph.error.GraphExceptionHandler;
import igorekpotworek.graph.repository.GraphRepository;
import igorekpotworek.infrastructure.server.Controller;
import igorekpotworek.infrastructure.server.error.ExceptionHandler;
import igorekpotworek.infrastructure.server.lifecycle.CloseConnectionPredicate;
import igorekpotworek.infrastructure.server.lifecycle.LifecycleHook.ShutdownHook;
import igorekpotworek.infrastructure.server.lifecycle.LifecycleHook.StartupHook;
import io.vavr.collection.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.lang.String.format;

@Configuration
class GraphModule {

  @Bean
  GraphRepository graphRepository() {
    return new GraphRepository();
  }

  @Bean
  List<Controller> controllers(GraphRepository graphRepository) {
    return List.of(
        new AddEdgeController(graphRepository),
        new AddNodeController(graphRepository),
        new CloserThanController(graphRepository),
        new GreetingsController(),
        new RemoveEdgeController(graphRepository),
        new RemoveNodeController(graphRepository),
        new ShortestPathController(graphRepository));
  }

  @Bean
  ExceptionHandler exceptionHandler() {
    return new GraphExceptionHandler();
  }

  @Bean
  StartupHook onStart() {
    return (out, s) -> out.println(format("HI, I AM %s", s.getId()));
  }

  @Bean
  ShutdownHook onEnd() {
    return (out, s) ->
        out.println(format("BYE %s, WE SPOKE FOR %s MS", s.getClientName(), s.sessionTime()));
  }

  @Bean
  CloseConnectionPredicate closeConnectionPredicate() {
    return (r) -> r.getBody().equals("BYE MATE!");
  }
}
