package org.kotlin.auctions.auctions.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

/**
 * Created by vladimir_antin on 2.8.17..
 */
@Configuration
class RestConfig : WebMvcConfigurerAdapter() {
    override fun addViewControllers(registry: ViewControllerRegistry?) {
        registry!!.addViewController("/").setViewName("forward:/index.html")
        registry.addViewController("/home").setViewName("forward:/index.html")
        registry.addViewController("/404/**").setViewName("forward:/404.html")
        registry.addViewController("/401/**").setViewName("forward:/401.html")
        registry.addViewController("/login").setViewName("forward:/login.html")
    }
    //    @Bean
    //    public InternalResourceViewResolver viewResolver() {
    //        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
    //        resolver.setPrefix("/WEB-INF/jsp/");
    //        resolver.setSuffix(".jsp");
    //        return resolver;
    //    }

}
