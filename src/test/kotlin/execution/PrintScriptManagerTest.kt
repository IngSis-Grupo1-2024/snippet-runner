package execution

import cli.Version
import ingsis.interpreter.interpretStatement.Input
import ingsis.utils.OutputEmitter
import modules.execution.printscript.PrintScriptManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.io.ByteArrayInputStream
import java.io.InputStream

class PrintScriptManagerTest {
    private val printScriptManager = PrintScriptManager()

    @Test
    fun `format throws Exception with invalid rulePath`() {
        val inputStream: InputStream = ByteArrayInputStream("error: sample error message".toByteArray())
        val outputEmitter: OutputEmitter = mock(OutputEmitter::class.java)
        val input: Input = mock(Input::class.java)

        assertThrows(Exception::class.java) {
            printScriptManager.format("rulePath", inputStream, outputEmitter, "v1", input)
        }
    }

    @Test
    fun `format throws Exception with`() {
        val inputStream: InputStream = ByteArrayInputStream("println('yes')".toByteArray())
        val outputEmitter: OutputEmitter = mock(OutputEmitter::class.java)
        val input: Input = mock(Input::class.java)

        assertThrows(Exception::class.java) {
            printScriptManager.format("src/test/resources/format-rules.json", inputStream, outputEmitter, "v1", input)
        }
    }

    @Test
    fun `parseToPrintScriptVersion returns VERSION_2 for v2`() {
        val version = printScriptManager.parseToPrintScriptVersion("v2")
        assertEquals(Version.VERSION_2, version)
    }

    @Test
    fun `parseToPrintScriptVersion returns VERSION_1 for unsupported version`() {
        val version = printScriptManager.parseToPrintScriptVersion("v3")
        assertEquals(Version.VERSION_1, version)
    }
}
