package ru.attributive.access.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ru.attributive.access.controller.Child
import ru.attributive.access.controller.Model
import ru.attributive.access.interceptor.AttributiveInterceptor
import ru.attributive.access.model.AttributivAccess
import ru.attributive.access.model.Restricition
import java.util.*

@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(AttributiveInterceptor())
    }

    //todo logic for getting restriction (using resttemplate for example)
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    fun getAttrAcc(): AttributivAccess {
        return AttributivAccess(
                mapOf(Restricition(UUID.randomUUID(), UUID.randomUUID(), Model::class.qualifiedName, true) to
                        listOf(Restricition(UUID.randomUUID(), UUID.randomUUID(), Model::name.name, false)),
                        Restricition(UUID.randomUUID(), UUID.randomUUID(), Child::class.qualifiedName, true) to
                                listOf(Restricition(UUID.randomUUID(), UUID.randomUUID(), Child::name.name, false))), listOf())
    }
}