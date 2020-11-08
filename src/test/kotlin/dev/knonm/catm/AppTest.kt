package dev.knonm.catm

import kotlin.test.Test
import kotlin.test.assertNotNull

class AppTest {
    @Test fun testAppHasAGreeting() {
        val classUnderTest = Jailer()
        assertNotNull(classUnderTest, "app should have a greeting")
    }
}
