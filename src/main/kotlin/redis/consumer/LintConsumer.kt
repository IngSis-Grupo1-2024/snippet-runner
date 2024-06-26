package redis.consumer

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.austral.ingsis.redis.RedisStreamConsumer
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import redis.events.LintRequest
import redis.events.LintResult
import redis.events.LintResultStatus
import redis.producer.LintProducer
import redis.snippetGetter.SnippetGetter

class LintConsumer(
    redis: RedisTemplate<String, String>,
    @Value("\${redis.request_lint_key}") streamKey: String,
    @Value("\${redis.groups.lint}") groupId: String,
    private val executor: cli.AnalyzeCli,
    private val snippetGetter: SnippetGetter,
    private val producer: LintProducer
) : RedisStreamConsumer<LintRequest>(streamKey, groupId, redis) {

    init {
        subscription()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onMessage(record: ObjectRecord<String, LintRequest>) {
        println("Received record: ${record.value}")

        val eventPayload = record.value
        val content = snippetGetter.getSnippet(eventPayload.snippetId)

        try {
            val result = executor.analyzeInputStream(eventPayload.ruleConfig, content.byteInputStream())
            val resultEventStatus =
                if (result == "SUCCESSFUL ANALYSIS") LintResultStatus.SUCCESS else LintResultStatus.FAILURE

            GlobalScope.launch {
                producer.publishEvent(
                    LintResult(
                        eventPayload.userId,
                        eventPayload.snippetId,
                        resultEventStatus
                    )
                )
            }

        } catch (exception: Exception) {
            GlobalScope.launch {
                producer.publishEvent(
                    LintResult(
                        eventPayload.userId,
                        eventPayload.snippetId,
                        LintResultStatus.FAILURE
                    )
                )
            }
        } finally {
            println("Finished processing record: ${record.value}")
        }
    }

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LintRequest>> {
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(java.time.Duration.ofMillis(10000))
            .targetType(LintRequest::class.java)
            .build()
    }
}
