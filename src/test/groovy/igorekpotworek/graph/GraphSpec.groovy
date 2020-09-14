package igorekpotworek.graph

import igorekpotworek.graph.repository.GraphRepository
import org.junit.After
import org.springframework.beans.factory.annotation.Autowired

trait GraphSpec {

    @Autowired
    GraphRepository graphRepository

    @After
    def cleanupGraph() {
        graphRepository.removeAllVertices()
    }
}