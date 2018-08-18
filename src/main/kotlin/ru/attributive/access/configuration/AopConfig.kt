package ru.attributive.access.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import ru.attributive.access.aspect.ResultFiltering


@Configuration
@EnableAspectJAutoProxy
class AopConfig {
    @Bean
    fun getResultFilteringAspect() = ResultFiltering()
}