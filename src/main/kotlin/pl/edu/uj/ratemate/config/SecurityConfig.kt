package pl.edu.uj.ratemate.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.access.channel.ChannelProcessingFilter


@EnableWebSecurity
@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.addFilterBefore(CorsFilter(), ChannelProcessingFilter::class.java)
//        http
//                .authorizeRequests()
//                .antMatchers("/none")
//                .permitAll()
//                .anyRequest()
//                .fullyAuthenticated()
//                .and()
//                .httpBasic()
//                .and().csrf().disable()
    }
}