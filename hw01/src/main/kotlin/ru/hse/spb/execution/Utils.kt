package ru.hse.spb.execution

import java.util.*

object Utils {
    fun setenv(key: String, value: String) {
        val classes = Collections::class.java!!.getDeclaredClasses()
        val env = System.getenv()
        for (cl in classes) {
            if ("java.util.Collections\$UnmodifiableMap" == cl.getName()) {
                val field = cl.getDeclaredField("m")
                field.setAccessible(true)
                val obj = field.get(env)
                val map = obj as MutableMap<String, String>
                map[key] = value
            }
        }
    }
}