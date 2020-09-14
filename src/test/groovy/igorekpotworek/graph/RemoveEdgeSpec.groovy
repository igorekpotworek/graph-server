package igorekpotworek.graph

class RemoveEdgeSpec extends ControllerSpec implements GraphSpec, ClientSpec {

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
    }

    def 'should return error if node not found'() {
        when:
        def response = client.send("REMOVE EDGE A B")

        then:
        response == "ERROR: NODE NOT FOUND"
    }
}
