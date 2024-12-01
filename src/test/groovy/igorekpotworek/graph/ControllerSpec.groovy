package igorekpotworek.graph

import igorekpotworek.infrastructure.ServerExecutionListener
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.junit.jupiter.SpringExtension
import spock.lang.Specification

import static org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS

@ExtendWith(SpringExtension)
@ContextConfiguration(classes = GraphApp, initializers = ConfigDataApplicationContextInitializer)
@TestExecutionListeners(value = ServerExecutionListener, mergeMode = MERGE_WITH_DEFAULTS)
abstract class ControllerSpec extends Specification {


}
