package igorekpotworek.graph

class AddNodeSpec extends ControllerSpec implements GraphSpec, ClientSpec {

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
    }

}
