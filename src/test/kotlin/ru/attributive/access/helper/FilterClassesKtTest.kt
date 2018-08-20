package ru.attributive.access.helper

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import ru.attributive.access.controller.Child
import ru.attributive.access.controller.Model
import ru.attributive.access.model.AttributivAccess
import ru.attributive.access.model.Restricition
import java.util.*

@SpringBootTest(classes = [FilterClassesKtTest::class])
@RunWith(SpringRunner::class)
class FilterClassesKtTest {

    val restr = AttributivAccess(
            mapOf(Restricition(UUID.randomUUID(), UUID.randomUUID(), Model::class.qualifiedName, true) to
                    listOf(Restricition(UUID.randomUUID(), UUID.randomUUID(), Model::name.name, false)),
                    Restricition(UUID.randomUUID(), UUID.randomUUID(), Child::class.qualifiedName, true) to
                            listOf(Restricition(UUID.randomUUID(), UUID.randomUUID(), Child::name.name, false))), listOf())

    @Test
    fun filter() {
    }

    @Test
    fun filterBeforeSaveTest() {
        val objInDb = Model(1, "model", listOf(Child(2, 1, "child")))
        val objToSave = Model(1, "newModel", listOf(Child(2, 1, "newChild")))
        val afterFilter = filterBeforeSave(objInDb, objToSave, restr.modelAndPropertyRestricition!!)
        Assert.assertEquals(afterFilter, objInDb)

    }
}