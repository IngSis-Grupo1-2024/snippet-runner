package execution

import modules.execution.input.Language
import modules.execution.language.SelectLanguage
import modules.execution.printscript.PrintScriptManager
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SelectLanguageTest {
    @Test
    fun `selectLanguage returns PrintScriptManager for PRINTSCRIPT language`() {
        val languageManager = SelectLanguage.selectLanguage(Language.PRINTSCRIPT)
        assertTrue(languageManager is PrintScriptManager)
    }
}
