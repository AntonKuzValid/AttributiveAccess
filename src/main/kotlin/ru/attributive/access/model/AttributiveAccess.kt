package ru.attributive.access.model

open class AttributivAccess(
        open var modelAndPropertyRestricition: Map<Restricition, List<Restricition>>? = null,
        open var methodRestricition: List<Restricition>? = null
)