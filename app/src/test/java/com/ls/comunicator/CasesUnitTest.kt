package com.ls.comunicator

import com.ls.comunicator.core.CaseEnum
import com.ls.comunicator.core.getCase
import com.ls.comunicator.model.Cases
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.function.Executable

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CasesUnitTest {

    private val cases = Cases()

    @BeforeAll
    fun init() {
        cases.nominative = "стул"
        cases.genitive = "стула"
        cases.dative = "стулу"
        cases.accusative = "стул"
        cases.instrumental = "стулом"
        cases.prepositional = "о стуле"
    }

    @Test
    fun `Get the case string from the object with the required case`() {
        Assertions.assertAll(
            Executable { assertEquals(cases.nominative, getCase(CaseEnum.NOMINATIVE, cases)) },
            Executable { assertEquals(cases.genitive, getCase(CaseEnum.GENITVIE, cases)) },
            Executable { assertEquals(cases.dative, getCase(CaseEnum.DATIVE, cases)) },
            Executable { assertEquals(cases.accusative, getCase(CaseEnum.ACCUSATIVE, cases)) },
            Executable { assertEquals(cases.instrumental, getCase(CaseEnum.INSTRUMENTAL, cases)) },
            Executable { assertEquals(cases.prepositional, getCase(CaseEnum.PREPOSITIONAL, cases)) }
        )
    }
}
