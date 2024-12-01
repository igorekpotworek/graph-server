package igorekpotworek.graph

import igorekpotworek.graph.repository.GraphRepository
import org.springframework.beans.factory.annotation.Autowired

class AddEdgeSpec extends ControllerSpec implements ClientSpec {

    @Autowired
    GraphRepository graphRepository

    def 'should add edge'() {
        when:
        client.send("ADD NODE A")
        client.send("ADD NODE B")
        def response = client.send("ADD EDGE A B 10")

        then:
        response == "EDGE ADDED"

        and:
        graphRepository.containsEdge("A", "B")

        when:
        def secondEdgeResponse = client.send("ADD EDGE A B 10")

        then:
        secondEdgeResponse == "EDGE ADDED"

        cleanup:
        graphRepository.removeAllVertices()
    }

    def 'should not add edge if weight is negative'() {
        when:
        client.send("ADD NODE A")
        client.send("ADD NODE B")
        def response = client.send("ADD EDGE A B -10")

        then:
        response == "SORRY, I DID NOT UNDERSTAND THAT"

        and:
        !graphRepository.containsEdge("A", "B")

        cleanup:
        graphRepository.removeAllVertices()
    }

    def 'should not add edge if weight is zero'() {
        when:
        client.send("ADD NODE A")
        client.send("ADD NODE B")
        def response = client.send("ADD EDGE A B 0")

        then:
        response == "SORRY, I DID NOT UNDERSTAND THAT"

        and:
        !graphRepository.containsEdge("A", "B")

        cleanup:
        graphRepository.removeAllVertices()
    }

    def 'should not add edge if node not found'() {
        when:
        def response = client.send("ADD EDGE A B 10")

        then:
        response == "ERROR: NODE NOT FOUND"

        and:
        !graphRepository.containsEdge("A", "B")
    }
}
