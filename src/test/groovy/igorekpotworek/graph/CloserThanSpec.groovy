package igorekpotworek.graph

class CloserThanSpec extends ControllerSpec implements GraphSpec, ClientSpec {

    def 'should return nodes closer than given radius'() {
        when:
        client.send("ADD NODE A")
        client.send("ADD NODE B")
        client.send("ADD NODE C")
        client.send("ADD NODE D")
        client.send("ADD EDGE A B 5")
        client.send("ADD EDGE B C 2")
        client.send("ADD EDGE C D 8")
        def response = client.send("CLOSER THAN 15 A")

        then:
        response == "B,C"
    }

    def 'should return error if node not found'() {
        when:
        def response = client.send("CLOSER THAN 8 A")

        then:
        response == "ERROR: NODE NOT FOUND"
    }
}
