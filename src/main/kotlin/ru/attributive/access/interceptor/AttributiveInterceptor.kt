package ru.attributive.access.interceptor

import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import ru.attributive.access.model.AttributivAccess
import javax.annotation.Resource
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AttributiveInterceptor : HandlerInterceptorAdapter() {

    @Resource(name = "getAttrAcc")
    lateinit var access: AttributivAccess

    //todo add here method checking
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        println("preHandle")
        return true
    }
}