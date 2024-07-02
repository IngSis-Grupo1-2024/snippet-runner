package security
import modules.security.AudienceValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.jwt.Jwt
import java.time.Instant

class TestAudience {
    private val audience = "expected-audience"
    private val audienceValidator = AudienceValidator(audience)

    private fun createJwt(audience: Set<String>): Jwt {
        return Jwt.withTokenValue("token")
            .header("header", "none")
            .audience(audience)
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(8000))
            .build()
    }

    @Test
    fun `000 - Present audience - should not have errors`() {
        val jwt = createJwt(setOf(audience))
        val validation = audienceValidator.validate(jwt)

        assertEquals(false, validation.hasErrors())
    }

    @Test
    fun `001 - Audience not present - should be invalid`() {
        val jwt = createJwt(setOf("other-audience"))
        val validation = audienceValidator.validate(jwt)
        assertTrue(validation.hasErrors())
        assertEquals("invalid_token", validation.errors.first().errorCode)
        assertEquals("The required audience is missing", validation.errors.first().description)
    }
}
