package org.kotlin.auctions.auctions

import org.kotlin.auctions.auctions.security.JwtFilter
import org.kotlin.auctions.auctions.storage.StorageProperties
import org.kotlin.auctions.auctions.storage.StorageService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties::class)
class AuctionsApplication {

    @Bean
    fun jwtFilter(): FilterRegistrationBean {
        val registrationBean = FilterRegistrationBean()
        registrationBean.filter = JwtFilter()
        registrationBean.addUrlPatterns("/api/*")

        return registrationBean
    }

    @Bean
    internal fun init(storageService: StorageService)= CommandLineRunner {
        storageService.deleteAll()
        storageService.init()
    }

    companion object {

        @Throws(Exception::class)
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(AuctionsApplication::class.java, *args)
        }
    }
}