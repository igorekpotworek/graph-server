package igorekpotworek.graph;

import igorekpotworek.infrastructure.server.Server;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraphApp implements CommandLineRunner {
    @Autowired
    private Server server;

    public static void main(String[] args) {
        val app = new SpringApplication(GraphApp.class);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        server.start();
    }
}
