package rom.biz.validation

import rom.common.Context
import rom.common.helpers.errorValidation
import rom.common.helpers.fail
import rom.common.models.ModelLock
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.validateLockProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в MkplAdId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { modelValidating.lock != ModelLock.NONE && !modelValidating.lock.asString().matches(regExp) }
    handle {
        val encodedId = modelValidating.lock.asString()
        fail(
            errorValidation(
                field = "lock",
                violationCode = "badFormat",
                description = """
                    value $encodedId must contain only:
                        digits (0-9)
                        lowercase letters (a-z)
                        capital letters (A-Z)
                        '-' symbol
                """.trimIndent()
            )
        )
    }
}
