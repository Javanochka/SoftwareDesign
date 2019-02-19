package ru.hse.spb.execution

import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class CdTest {

    @Test
    fun cdToInsideDirectory() {
        val expected = Paths.get(Paths.get("").toAbsolutePath().toString(), "src", "test", "resources").toAbsolutePath().toString() + "\n"
        val old = Pwd(null).execute()
        Cd(listOf("src" + File.separator + "test" + File.separator + "resources"), null).execute()
        assertEquals(expected, Pwd(null).execute())
        System.setProperty("user.dir", old)
    }

    @Test
    fun cdToOuterDirectory() {
        val expected = Paths.get(Paths.get("").toAbsolutePath().toString(), "..").toAbsolutePath().toString() + "\n"
        val old = Pwd(null).execute()
        Cd(listOf(".."), null).execute()
        assertEquals(expected, Pwd(null).execute())
        System.setProperty("user.dir", old)
    }

    @Test
    fun cdToHomeDirectory() {
        val expected = System.getProperty("user.home") + "\n"
        val old = Pwd(null).execute()
        Cd(listOf(), null).execute()
        assertEquals(expected, Pwd(null).execute())
        System.setProperty("user.dir", old)
    }
}