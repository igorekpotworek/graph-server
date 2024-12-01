package igorekpotworek.graph

import igorekpotworek.graph.repository.GraphRepository
import org.springframework.beans.factory.annotation.Autowired

class ShortestPathSpec extends ControllerSpec implements ClientSpec {

    @Autowired
    GraphRepository graphRepository

    def 'should return shortest path'() {
        when:
        client.send("ADD NODE A")
        client.send("ADD NODE B")
        client.send("ADD NODE C")
        client.send("ADD NODE D")
        client.send("ADD EDGE A B 10")
        client.send("ADD EDGE A D 1")
        client.send("ADD EDGE D B 2")
        def response = client.send("SHORTEST PATH A B")

        then:
        response == "3"

        cleanup:
        graphRepository.removeAllVertices()
    }

    def 'should return error if node not found'() {
        when:
        def response = client.send("SHORTEST PATH A B")

        then:
        response == "ERROR: NODE NOT FOUND"
    }

    def 'should return max int value if there is no edge'() {
        when:
        client.send("ADD NODE A")
        client.send("ADD NODE B")
        def response = client.send("SHORTEST PATH A B")

        then:
        response == Integer.MAX_VALUE.toString()

        cleanup:
        graphRepository.removeAllVertices()
    }

}
