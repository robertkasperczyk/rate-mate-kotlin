package pl.edu.uj.ratemate.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CorsFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
class ApiConfig {

//    @Bean
//    fun corsFilter(): CorsFilter {
//        val source = UrlBasedCorsConfigurationSource()
//        val config = CorsConfiguration()
//        config.allowCredentials = true
//        config.addAllowedOrigin("*")
//        config.addAllowedHeader("*")
//        config.addAllowedMethod("OPTIONS")
//        config.addAllowedMethod("GET")
//        config.addAllowedMethod("POST")
//        config.addAllowedMethod("PUT")
//        config.addAllowedMethod("DELETE")
//        config.maxAge = 180
//
//        source.registerCorsConfiguration("/**", config)
//        return CorsFilter(source)
//    }
}