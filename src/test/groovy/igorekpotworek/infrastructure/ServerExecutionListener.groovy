package igorekpotworek.infrastructure

import igorekpotworek.infrastructure.server.Server
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener

import java.util.concurrent.Executors

class ServerExecutionListener implements TestExecutionListener {

    private static boolean initialized = false

    @Override
    void beforeTestClass(TestContext testContext) throws Exception {
        if (!initialized) {
            def server = testContext.getApplicationContext().getBean(Server)
            Executors.newSingleThreadExecutor().submit({ server.start() })
            initialized = true
        }
    }

}
