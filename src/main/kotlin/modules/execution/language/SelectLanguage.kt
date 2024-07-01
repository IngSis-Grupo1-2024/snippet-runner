package modules.execution.language

import modules.execution.input.Language
import modules.execution.printscript.PrintScriptManager

class SelectLanguage {
    companion object {
        fun selectLanguage(language: Language): LanguageManager {
            return when (language) {
                Language.PRINTSCRIPT -> PrintScriptManager()
            }
        }
    }
}
