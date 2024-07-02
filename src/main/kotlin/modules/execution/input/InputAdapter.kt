package modules.execution.input

import ingsis.interpreter.interpretStatement.Input
import java.util.*

class InputAdapter(private val input: Queue<String>) : Input {
    override fun readInput(string: String): String {
        return input.poll()
    }
}
