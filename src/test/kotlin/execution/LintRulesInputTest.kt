package execution

import modules.execution.input.LintRulesInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LintRulesInputTest {
    @Test
    fun `test object instantiation`() {
        val lintRulesInput =
            LintRulesInput(
                name = "RuleName",
                isActive = true,
                expression = false,
                identifier = true,
                literal = false,
                format = "FormatString",
            )

        assertEquals("RuleName", lintRulesInput.name)
        assertEquals(true, lintRulesInput.isActive)
        assertEquals(false, lintRulesInput.expression)
        assertEquals(true, lintRulesInput.identifier)
        assertEquals(false, lintRulesInput.literal)
        assertEquals("FormatString", lintRulesInput.format)
    }
}
