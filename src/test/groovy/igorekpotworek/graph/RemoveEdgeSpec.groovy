package igorekpotworek.graph

import igorekpotworek.graph.repository.GraphRepository
import org.springframework.beans.factory.annotation.Autowired

class RemoveEdgeSpec extends ControllerSpec implements ClientSpec {

    @Autowired
    GraphRepository graphRepository

    def 'should remove edge'() {
        when:
        client.send("ADD NODE A")
        client.send("ADD NODE B")
        client.send("ADD EDGE A B 10")
        def response = client.send("REMOVE EDGE A B")

        then:
        response == "EDGE REMOVED"

        and:
        !graphRepository.containsEdge("A", "B")

        cleanup:
        graphRepository.removeAllVertices()
    }

    def 'should succeed even if edge not found'() {
        when:
        client.send("ADD NODE A")
        client.send("ADD NODE B")
        def response = client.send("REMOVE EDGE A B")

        then:
        response == "EDGE REMOVED"

        and:
        !graphRepository.containsEdge("A", "B")

        cleanup:
        graphRepository.removeAllVertices()
    }

    def 'should return error if node not found'() {
        when:
        def response = client.send("REMOVE EDGE A B")

        then:
        response == "ERROR: NODE NOT FOUND"

        cleanup:
        graphRepository.removeAllVertices()
    }
}
