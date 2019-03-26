package ru.hse.spb.execution

import org.junit.Assert.*
import org.junit.Test
import java.io.File
import java.nio.file.Paths

class LsTest {
    @Test
    fun lsCurrentDirectory() {
        Cd(listOf("src" + File.separator + "test" + File.separator + "resources"), null).execute()
        assertEquals("ru directory\n", Ls(listOf(), null).execute())
        Cd(listOf(Paths.get("..", "..", "..").toString()), null).execute()
    }

    @Test
    fun lsOtherDirectory() {
        val expected = listOf("", "example.txt file", "testFile1 file", "testFile2 file").sorted()
        val result = Ls(listOf("src" + File.separator + "test" + File.separator + "resources" + File.separator + "ru" + File.separator + "hse" + File.separator + "spb"), null).execute().split("\n").sorted()
        assertEquals(expected, result)
    }
}