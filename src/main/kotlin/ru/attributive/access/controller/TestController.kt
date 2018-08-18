package ru.attributive.access.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

@RestController
class TestController {
    @GetMapping
    fun getModel(): Model = Model(1, "model", listOf(Child(2, 1, "child")))
}

@Entity
data class Model(
        @Id
        var id: Int? = null,
        var name: String? = null,
        @OneToMany
        @JoinColumn(name = "pId")
        var roles: List<Child>
)

@Entity
data class Child(
        @Id
        var id: Int? = null,
        var pId: Int? = null,
        var name: String? = null
)
