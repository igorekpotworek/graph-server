package igorekpotworek.graph

import igorekpotworek.graph.repository.GraphRepository
import org.springframework.beans.factory.annotation.Autowired

class AddNodeSpec extends ControllerSpec implements ClientSpec {

    @Autowired
    GraphRepository graphRepository

    def 'should add node'() {
        when:
        def response = client.send("ADD NODE A")

        then:
        response == "NODE ADDED"

        and:
        graphRepository.containsNode("A")

        when:
        def errorResponse = client.send("ADD NODE A")

        then:
        errorResponse == "ERROR: NODE ALREADY EXISTS"

        cleanup:
        graphRepository.removeAllVertices()
    }

}
