package rom.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class ModelLock(private val id: String) {
    fun asString() = id

    companion object {
        val NONE = ModelLock("")
    }
}
