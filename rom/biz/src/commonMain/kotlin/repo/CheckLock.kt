package rom.biz.repo

import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.State
import rom.common.repo.errorRepoConcurrency
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.checkLock(title: String) = worker {
    this.title = title
    description = """
        Проверка оптимистичной блокировки. Если не равна сохраненной в БД, значит данные запроса устарели 
        и необходимо их обновить вручную
    """.trimIndent()
    on { state == State.RUNNING && modelValidated.lock != modelRepoRead.lock }
    handle {
        fail(errorRepoConcurrency(modelRepoRead, modelValidated.lock).errors)
    }
}
