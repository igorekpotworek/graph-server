package igorekpotworek.graph

import igorekpotworek.infrastructure.Client
import spock.lang.Shared

trait ClientSpec {
    @Shared
    Client client = new Client()

    def setup() {
        client.startConnection()
    }

    def cleanup() {
        client.stopConnection()
    }
}