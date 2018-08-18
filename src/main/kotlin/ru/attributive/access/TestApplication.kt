package ru.attributive.access

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["ru.attributive.access"])
class TestApplication

fun main(args: Array<String>) {
    runApplication<TestApplication>(*args)
}