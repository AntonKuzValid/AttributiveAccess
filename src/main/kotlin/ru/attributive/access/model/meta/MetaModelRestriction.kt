package ru.attributive.access.model.meta

class MetaModelRestriction(
        var moduleName: String? = null,
        var className: String? = null,
        var properties: Set<MetaPropertyRestriction>? = null
)