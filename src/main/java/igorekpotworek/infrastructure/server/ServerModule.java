package igorekpotworek.infrastructure.server;

import igorekpotworek.infrastructure.server.error.ExceptionHandler;
import igorekpotworek.infrastructure.server.lifecycle.CloseConnectionPredicate;
import igorekpotworek.infrastructure.server.lifecycle.LifecycleHook.ShutdownHook;
import igorekpotworek.infrastructure.server.lifecycle.LifecycleHook.StartupHook;
import io.vavr.collection.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static igorekpotworek.infrastructure.server.Response.error;

@Configuration
public class ServerModule {

  @Bean
  @ConfigurationProperties(prefix = "server")
  ServerConfig serverConfig() {
    return new ServerConfig();
  }

  @Bean
  @ConditionalOnMissingBean(value = Controller.class, parameterizedContainer = List.class)
  List<Controller> defaultControllers() {
    return List.of();
  }

  @Bean
  @ConditionalOnMissingBean(ShutdownHook.class)
  ShutdownHook defaultShutdownHook() {
    return (__, s) -> {};
  }

  @Bean
  @ConditionalOnMissingBean(StartupHook.class)
  StartupHook defaultStartupHook() {
    return (__, s) -> {};
  }

  @Bean
  @ConditionalOnMissingBean(ExceptionHandler.class)
  ExceptionHandler defaultExceptionHandler() {
    return (__) -> error("ERROR: INTERNAL SERVER ERROR");
  }

  @Bean
  @ConditionalOnMissingBean(CloseConnectionPredicate.class)
  CloseConnectionPredicate defaultCloseConnectionPredicate() {
    return (__) -> false;
  }

  @Bean
  Router router(
      List<Controller> controllers,
      ExceptionHandler exceptionHandler,
      CloseConnectionPredicate closeConnectionPredicate) {
    return new Router(controllers, exceptionHandler, closeConnectionPredicate);
  }

  @Bean
  Server server(ServerConfig serverConfig, Router router, StartupHook onStart, ShutdownHook onEnd) {
    return Server.builder()
        .port(serverConfig.getPort())
        .socketTimeout(serverConfig.getSocketTimeout())
        .numberOfThreads(serverConfig.getNumberOfThreads())
        .doOnStart(onStart)
        .doOnClose(onEnd)
        .router(router)
        .build();
  }
}
