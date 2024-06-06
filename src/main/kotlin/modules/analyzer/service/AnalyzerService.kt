package modules.analyzer.service

import modules.analyzer.repository.AnalyzerRepository
import org.springframework.stereotype.Service
import utils.OutputString

@Service
class AnalyzerService(private val analyzerRepository: AnalyzerRepository) {
    fun analyze(snippetId: Int): OutputString = analyzerRepository.analyze(snippetId, OutputString())
}