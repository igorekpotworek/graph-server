package igorekpotworek.graph

import igorekpotworek.infrastructure.Client
import spock.lang.Shared

class GreetingsSpec extends ControllerSpec {

    @Shared
    def client = new Client()

    def 'should establish connection, send greetings, close connection'() {
        when:
        def startResponse = client.startConnection()

        then:
        startResponse ==~ "HI, I AM [a-zA-Z\\-0-9]*"

        when:
        def greetingsResponse = client.send("HI, I AM IGOR")

        then:
        greetingsResponse == "HI IGOR"

        when:
        def unknownResponse = client.send("UNKNOWN COMMAND")

        then:
        unknownResponse == "SORRY, I DID NOT UNDERSTAND THAT"

        when:
        def closeResponse = client.send("BYE MATE!")

        then:
        closeResponse ==~ "BYE IGOR, WE SPOKE FOR \\d+ MS"
    }
}
