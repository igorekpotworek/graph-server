package igorekpotworek.graph

import igorekpotworek.infrastructure.Client
import org.junit.After
import org.junit.Before
import spock.lang.Shared

trait ClientSpec {
    @Shared
    Client client = new Client()

    @Before
    def setupClient() {
        client.startConnection()
    }

    @After
    def cleanupClient() {
        client.stopConnection()
    }
}