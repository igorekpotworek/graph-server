package igorekpotworek.graph

class RemoveNodeSpec extends ControllerSpec implements GraphSpec, ClientSpec {

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
    }

    def 'should return error if node not found'() {
        when:
        def response = client.send("REMOVE NODE A")

        then:
        response == "ERROR: NODE NOT FOUND"
    }
}
