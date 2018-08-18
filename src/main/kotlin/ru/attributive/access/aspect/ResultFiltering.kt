package ru.attributive.access.aspect

import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import ru.attributive.access.helper.filter
import ru.attributive.access.model.AttributivAccess
import javax.annotation.Resource

@Aspect
class ResultFiltering {
    @Resource(name = "getAttrAcc")
    lateinit var access: AttributivAccess


    @Pointcut("within(ru.attributive.access.controller..*)")
    fun filterControllerLayer() {
    }

    @AfterReturning(pointcut = "ru.attributive.access.aspect.ResultFiltering.filterControllerLayer()", returning = "retVal")
    fun filterResult(retVal: Any?): Any? {
        return try {
            val modelAndPropertyRestricition = access.modelAndPropertyRestricition
            filter(retVal!!, modelAndPropertyRestricition!!)
        } catch (e: Exception) {
            null
        }
    }
}