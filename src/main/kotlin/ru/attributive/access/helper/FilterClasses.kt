package ru.attributive.access.helper

import ru.attributive.access.model.Restricition
import java.lang.reflect.Field

class FilteringHelper

fun <T : Any> filter(obj: T, restricition: Map<Restricition, List<Restricition>>): T? {
    val clazz = obj::class.java
    val qualifiedName = clazz.name
    for ((classRest, propRestr) in restricition) {
        if (qualifiedName == classRest.name) {
            return if (classRest.allowed) {
                clazz.declaredFields.forEach { field ->
                    field.isAccessible = true
                    val currRestr = propRestr.getRestrictionByName(field.name)
                    if (currRestr?.allowed == false) field.setNull(obj)
                    else {
                        val fieldInst = field.get(obj)
                        if (fieldInst is Collection<*>) {
                            val result = arrayListOf<Any>()
                            val collection = fieldInst.toMutableList()
                            collection.forEach { elem ->
                                filter(elem!!, restricition)?.let { result.add(it) }
                            }
                            field.set(obj, result)
                        } else if (!fieldInst.javaClass.isPrimitive && !fieldInst.javaClass.isEnum) {
                            if (filter(fieldInst, restricition) == null) field.setNull(obj)
                        }
                    }
                }
                return obj
            } else null
        }
    }
    return obj
}

fun Field.setNull(obj: Any) = this.set(obj, null)

fun Collection<Restricition>.getRestrictionByName(str: String): Restricition? {
    forEach { if (it.name == str) return it }
    return null
}