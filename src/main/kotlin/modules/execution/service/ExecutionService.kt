package modules.execution.service

import modules.execution.repository.ExecutionRepository
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.stereotype.Service
import utils.OutputString
import utils.exceptions.NoFileFound
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

@Service
class ExecutionService(private val executionRepository: ExecutionRepository) {
    fun execute(snippetId: Int): OutputString {
        return executionRepository.execute(snippetId, OutputString())
    }
}