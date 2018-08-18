package ru.attributive.access.helper

import org.reflections.Reflections
import ru.attributive.access.model.meta.MetaMethodRestriction
import ru.attributive.access.model.meta.MetaModelRestriction
import ru.attributive.access.model.meta.MetaPropertyRestriction
import java.lang.reflect.Method


class ScannerProjectClasses {

    val moduleName = "moduleName" //todo add module name to properties

    var models: MutableList<Class<*>> = arrayListOf()
    var methods: MutableList<Method> = arrayListOf()

    fun scanModelPackage(packageName: String) {
        val reflections = Reflections(packageName)
        models.addAll(reflections.getSubTypesOf(Any::class.java))
    }

    fun addModel(clazz: Class<*>) {
        models.add(clazz)
    }

    fun addModels(classes: List<Class<*>>) {
        this.models.addAll(classes)
    }

    fun addModels(vararg classes: Class<*>) {
        this.models.addAll(classes.toList())
    }

    fun scanMethodPackage(packageName: String) {
        val reflections = Reflections(packageName)
        methods.addAll(reflections.getSubTypesOf(Any::class.java)
                .flatMap { it.declaredMethods.asList() })
    }

    fun addMethod(method: Method) {
        methods.add(method)
    }

    fun addMethods(methods: List<Method>) {
        this.methods.addAll(methods)
    }

    fun addMethods(vararg methods: Method) {
        this.methods.addAll(methods.toList())
    }

    private fun registerClasses() {
        val models = mutableListOf<MetaModelRestriction>()
        val methods = mutableListOf<MetaMethodRestriction>()
        if (models.isNotEmpty()) {
            this.models.map { cl ->
                MetaModelRestriction(
                        moduleName,
                        cl.name,
                        cl.declaredFields.map {
                            MetaPropertyRestriction(it.name)
                        }.toSet())
            }
        }
        if (methods.isNotEmpty()) {
            this.methods.map { mt ->
                MetaMethodRestriction(
                        moduleName,
                        mt.name
                )
            }
        }
        sendForRegistration(models, methods)
    }

    private fun sendForRegistration(models: List<MetaModelRestriction>, methods: List<MetaMethodRestriction>) {
        //todo send data to some service
    }
}