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

fun Field.setObj(toObj: Any, obj: Any) = this.set(toObj, this.get(obj))

fun Collection<Restricition>.getRestrictionByName(str: String): Restricition? {
    forEach { if (it.name == str) return it }
    return null
}

fun <T : Any> filterBeforeSave(objInDb: T, objToSave: T, restricition: Map<Restricition, List<Restricition>>): T {
    val clazzToSave = objToSave::class.java
    val qualifiedName = clazzToSave.name
    for ((classRest, propRestr) in restricition) {
        if (qualifiedName == classRest.name) {
            return if (classRest.allowed) {
                clazzToSave.declaredFields.forEach { field ->
                    field.isAccessible = true
                    val currRestr = propRestr.getRestrictionByName(field.name)
                    if (currRestr?.allowed == false) field.setObj(objToSave, objInDb)
                    else {
                        val fieldInstToSave = field.get(objToSave)
                        val fieldInstInBd = field.get(objInDb)
                        if (fieldInstToSave is Collection<*> && fieldInstInBd is Collection<*>) {
                            val result = arrayListOf<Any>()
                            val collToSave = fieldInstToSave.toMutableList()
                            val collInBd = fieldInstInBd.toMutableList()
                            collToSave.forEachIndexed { index, elem ->
                                result.add(filterBeforeSave(collInBd[index]!!, elem!!, restricition))
                            }
                            field.set(objToSave, result)
                        }
//                        } else if (!fieldInstToSave.javaClass.isPrimitive && !fieldInstToSave.javaClass.isEnum) {
//                            field.setObj(objToSave, filterBeforeSave(field.get(objInDb), field.get(objToSave), restricition))
//                        }
                    }
                }
                return objToSave
            } else objInDb
        }
    }
    return objToSave
}

//todo попытка зарефакторить
//private fun <T : Any> filter(returnObj: T? = null, targetObj: T, restricition: Map<Restricition, List<Restricition>>, setObj: Field.(obj: T, obj2: T) -> Unit): T? {
//    val clazzToSave = targetObj::class.java
//    val qualifiedName = clazzToSave.name
//    for ((classRest, propRestr) in restricition) {
//        if (qualifiedName == classRest.name) {
//            return if (classRest.allowed) {
//                clazzToSave.declaredFields.forEach { field ->
//                    field.isAccessible = true
//                    val currRestr = propRestr.getRestrictionByName(field.name)
//                    if (currRestr?.allowed == false) field.setObj(targetObj, returnObj)
//                    else {
//                        val fieldInstTarget = field.get(targetObj)
//                        val fieldInstReturn = returnObj?.let { field.get(returnObj) }
//                        if (fieldInstTarget is Collection<*>) {
//                            val result = arrayListOf<Any>()
//                            val collectionTarget = fieldInstTarget.toMutableList()
//                            val collectionReturn = fieldInstReturn?.let { it as Collection<*> }?.toMutableList()
//                            collectionTarget.forEachIndexed { index, elem ->
//                                val childObj = filter(collectionReturn?.let { it[index] }, elem!!, restricition, Field::setObj)
//                                if (childObj != null) result.add(childObj)
//                            }
//                            field.set(targetObj, result)
//                        } else if (!fieldInstTarget.javaClass.isPrimitive && !fieldInstTarget.javaClass.isEnum) {
//                            field.setObj(targetObj, filter(field.get(returnObj), field.get(targetObj), restricition, Field::setObj))
//                        }
//                    }
//                }
//                return targetObj
//            } else returnObj
//        }
//    }
//    return targetObj
//}
