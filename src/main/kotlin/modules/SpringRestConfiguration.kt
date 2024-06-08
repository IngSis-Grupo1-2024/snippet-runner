package modules

import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.CorsRegistry


@Configuration
class SpringRestConfiguration: RepositoryRestConfigurer {
    override fun configureRepositoryRestConfiguration(config: RepositoryRestConfiguration, cors: CorsRegistry?) {
        config.setDefaultMediaType(MediaType.APPLICATION_JSON)
        config.useHalAsDefaultJsonMediaType(false)
    }
}