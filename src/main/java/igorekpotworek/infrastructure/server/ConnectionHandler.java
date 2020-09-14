package igorekpotworek.infrastructure.server;

import igorekpotworek.infrastructure.server.lifecycle.LifecycleHook.ShutdownHook;
import igorekpotworek.infrastructure.server.lifecycle.LifecycleHook.StartupHook;
import io.vavr.collection.List;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.val;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Value
class ConnectionHandler implements Runnable {

    Socket clientSocket;
    Session session;
    Router router;
    List<StartupHook> doOnStart;
    List<ShutdownHook> doOnClose;

    ConnectionHandler(
            Socket clientSocket,
            Router router,
            List<StartupHook> doOnStart,
            List<ShutdownHook> doOnClose) {
        this.clientSocket = clientSocket;
        this.router = router;
        this.doOnStart = doOnStart;
        this.doOnClose = doOnClose;
        this.session = Session.getInstance();
    }

    @SneakyThrows
    public void run() {
        try (val out = new PrintWriter(clientSocket.getOutputStream(), true);
             val in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            doOnStart.forEach(it -> it.accept(out, session));
            router.handleRequest(in, out, session);
            doOnClose.forEach(it -> it.accept(out, session));
        } finally {
            clientSocket.close();
        }
    }
}
