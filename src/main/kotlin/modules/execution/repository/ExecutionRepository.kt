package modules.execution.repository

import org.springframework.stereotype.Repository
import utils.OutputString
import java.io.InputStream

@Repository
class ExecutionRepository {
    fun execute(
        inputStream: InputStream,
        output: OutputString,
    ) {
        output.print("Print Script is not set up")
    }
}
