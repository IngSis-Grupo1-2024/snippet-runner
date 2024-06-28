package redis.snippetGetter

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

class SnippetGetter(private val urlForBucket: String, private val rest: RestTemplate) {
    fun getSnippet(snippetId: String): String {
        val url = "$urlForBucket/$snippetId"
        val response = rest.getForEntity(url, String::class.java)
        if (response.statusCode != HttpStatus.OK) {
            throw HttpClientErrorException(response.statusCode, response.body!!)
        }
        return response.body!!
    }
}
