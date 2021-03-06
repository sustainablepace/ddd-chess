package net.sustainablepace.chess

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.resource.PathResourceResolver
import java.util.concurrent.TimeUnit


@SpringBootApplication
class ChessApplication

fun main(args: Array<String>) {
	runApplication<ChessApplication>(*args)
}

@Configuration
class CustomWebMVCConfig : WebMvcConfigurer {
	override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
		registry.addResourceHandler("/**")
			.addResourceLocations("classpath:/static/")
			.setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS)
				.cachePrivate()
				.mustRevalidate())
			.resourceChain(true)
			.addResolver(PathResourceResolver())
	}
}