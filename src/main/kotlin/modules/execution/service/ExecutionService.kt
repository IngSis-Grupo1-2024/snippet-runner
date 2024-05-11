package modules.execution.service

import modules.execution.repository.ExecutionRepository
import org.springframework.stereotype.Service
import utils.OutputString
import utils.exceptions.NoFileFound
import java.io.File
import java.io.FileInputStream

@Service
class ExecutionService(private val executionRepository: ExecutionRepository) {
    fun execute(filePath: String): OutputString {
        val file = File(filePath)
        if (!file.exists()) {
            throw NoFileFound("The path $filePath does not exist")
        }
        executionRepository.execute(FileInputStream(file), OutputString())
        return OutputString()
    }
}
