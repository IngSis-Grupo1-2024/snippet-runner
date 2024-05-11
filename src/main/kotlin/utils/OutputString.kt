package utils

class OutputString {
    private val output = StringBuilder()
    fun print(string: String) {
        output.append(string)
    }
    fun getMessage(): String = output.toString()
}