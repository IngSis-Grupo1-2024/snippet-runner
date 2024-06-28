package modules.execution.language

import modules.execution.model.Language
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
