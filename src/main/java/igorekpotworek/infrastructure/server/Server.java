package igorekpotworek.infrastructure.server;

import igorekpotworek.infrastructure.server.lifecycle.LifecycleHook.ShutdownHook;
import igorekpotworek.infrastructure.server.lifecycle.LifecycleHook.StartupHook;
import io.vavr.collection.List;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Value
@Slf4j
public class Server {
  int port;
  int socketTimeout;
  ExecutorService threadPool;

  List<StartupHook> doOnStart;
  List<ShutdownHook> doOnClose;
  Router router;

  private Server(ServerBuilder builder) {
    this.port = builder.port;
    this.socketTimeout = builder.socketTimeout;
    this.threadPool = Executors.newFixedThreadPool(builder.numberOfThreads);
    this.doOnStart = builder.doOnStart;
    this.doOnClose = builder.doOnClose;
    this.router = builder.router;
  }

  public static ServerBuilder builder() {
    return new ServerBuilder();
  }

  @SneakyThrows
  public void start() {
    log.info("Starting tcp server on port: {}", port);
    try (val serverSocket = new ServerSocket(port)) {
      while (true) {
        val clientSocket = serverSocket.accept();
        clientSocket.setSoTimeout(socketTimeout);
        threadPool.submit(new ConnectionHandler(clientSocket, router, doOnStart, doOnClose));
      }
    } finally {
      shutdown();
    }
  }

  private void shutdown() {
    log.info("Shutting down server");
    threadPool.shutdown();
    try {
      if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
        threadPool.shutdownNow();
      }
    } catch (InterruptedException ex) {
      threadPool.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }

  public static class ServerBuilder {
    private int port;
    private int numberOfThreads;
    private int socketTimeout;
    private Router router;

    private List<StartupHook> doOnStart = List.empty();
    private List<ShutdownHook> doOnClose = List.empty();

    private ServerBuilder() {}

    public ServerBuilder router(Router router) {
      this.router = router;
      return this;
    }

    public ServerBuilder port(int port) {
      this.port = port;
      return this;
    }

    public ServerBuilder socketTimeout(int socketTimeout) {
      this.socketTimeout = socketTimeout;
      return this;
    }

    public ServerBuilder numberOfThreads(int numberOfThreads) {
      this.numberOfThreads = numberOfThreads;
      return this;
    }

    public ServerBuilder doOnStart(StartupHook doOnStart) {
      this.doOnStart = this.doOnStart.append(doOnStart);
      return this;
    }

    public ServerBuilder doOnClose(ShutdownHook doOnClose) {
      this.doOnClose = this.doOnClose.append(doOnClose);
      return this;
    }

    public Server build() {
      return new Server(this);
    }
  }
}
