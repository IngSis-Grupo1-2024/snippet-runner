package modules.formatting.service

import modules.formatting.repository.FormatterRepository
import org.springframework.stereotype.Service
import utils.OutputString

@Service
class FormatterService(private val formatterRepository: FormatterRepository) {
    fun format(snippetId: Int): OutputString {
        val response: OutputString =  formatterRepository.format(snippetId, OutputString())
        if (response.getMessage() != "OK") {
            throw Exception("Error while formatting the code")
        }
        return response
    }
}