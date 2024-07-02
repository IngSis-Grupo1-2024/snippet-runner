package modules.redis.consumer

import com.example.redisevents.LintRequest
import com.example.redisevents.LintResult
import com.example.redisevents.LintResultStatus
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import modules.execution.input.Language
import modules.execution.input.LinterInput
import modules.execution.service.ExecutionService
import modules.redis.producer.LintProducer
import org.austral.ingsis.redis.RedisStreamConsumer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class LintConsumer(
    redis: RedisTemplate<String, String>,
    @Value("\${manager.redis.stream.request_lint_key}") streamKey: String,
    @Value("\${manager.redis.groups.lint}") groupId: String,
    private val executionService: ExecutionService,
    private val producer: LintProducer,
) : RedisStreamConsumer<LintRequest>(streamKey, groupId, redis) {
    init {
        subscription()
    }
    private val logger = LoggerFactory.getLogger(LintConsumer::class.java)

    override fun onMessage(record: ObjectRecord<String, LintRequest>) {
        logger.info("Received record: ${record.value}")

        val eventPayload = record.value
        val payloadParsed =
            getLinterInput(eventPayload)

        val result = executionService.lint(payloadParsed)
        if (result.statusCode.value() == 200) publishEvent(eventPayload, LintResultStatus.SUCCESS)
        else publishEvent(eventPayload, LintResultStatus.FAILURE)

        logger.info("Finished processing record: ${record.value}")
        println("Finished processing record: ${record.value}")
    }

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LintRequest>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(java.time.Duration.ofMillis(10000))
            .targetType(LintRequest::class.java)
            .build()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun publishEvent(eventPayload: LintRequest, lintResultStatus: LintResultStatus) {
        GlobalScope.launch {
            producer.publishEvent(
                LintResult(
                    eventPayload.userId,
                    eventPayload.snippetId,
                    lintResultStatus,
                ),
            )
        }
    }

    private fun getLinterInput(eventPayload: LintRequest) = LinterInput(
        eventPayload.content,
        languageParser(eventPayload.language),
        eventPayload.version,
        eventPayload.rules,
        eventPayload.input,
        eventPayload.snippetId,
        eventPayload.userId,
    )


    private fun languageParser(language: String): Language {
        return when (language) {
            "PRINTSCRIPT" -> Language.PRINTSCRIPT
            else -> {
                throw IllegalArgumentException("Language not supported")
            }
        }
    }
}
