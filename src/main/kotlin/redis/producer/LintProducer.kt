package redis.producer

import org.austral.ingsis.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import redis.events.LintResult

class LintProducer(
    @Value("\${redis.result_lint_key}") streamKey: String,
    redis: RedisTemplate<String, String>,
) : RedisStreamProducer(streamKey, redis) {
    fun publishEvent(event: LintResult) {
        println("userId: ${event.userId}, snippetKey: ${event.snippetId}, status: ${event.result}")
        emit(event)
    }
}
