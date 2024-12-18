package rom.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class ParamId(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = ParamId("")
    }
}
