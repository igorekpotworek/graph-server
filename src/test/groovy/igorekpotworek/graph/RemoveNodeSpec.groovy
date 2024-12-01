package igorekpotworek.graph

import igorekpotworek.graph.repository.GraphRepository
import org.springframework.beans.factory.annotation.Autowired

class RemoveNodeSpec extends ControllerSpec implements ClientSpec {

    @Autowired
    GraphRepository graphRepository

    def 'should remove node'() {
        when:
        client.send("ADD NODE A")
        client.send("ADD NODE B")
        client.send("ADD EDGE A B 10")

        def response = client.send("REMOVE NODE A")

        then:
        response == "NODE REMOVED"

        and:
        !graphRepository.containsNode("A")

        and:
        !graphRepository.containsEdge("A", "B")

        cleanup:
        graphRepository.removeAllVertices()
    }

    def 'should return error if node not found'() {
        when:
        def response = client.send("REMOVE NODE A")

        then:
        response == "ERROR: NODE NOT FOUND"
    }
}
