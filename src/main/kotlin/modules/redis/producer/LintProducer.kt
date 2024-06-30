package modules.redis.producer

import com.example.redisevents.LintResult
import org.austral.ingsis.redis.RedisStreamProducer
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class LintProducer(
    @Value("\${manager.redis.result_lint_key}") streamKey: String,
    redis: RedisTemplate<String, String>,
) : RedisStreamProducer(streamKey, redis) {
    fun publishEvent(event: LintResult) {
        println("userId: ${event.userId}, snippetKey: ${event.snippetId}, status: ${event.result}")
        emit(event)
    }
}
