package ru.attributive.access.model

import java.util.*

class Restricition(
        var id: UUID? = null,
        var userId: UUID? = null,
        var name: String? = null,
        var allowed: Boolean = true
)