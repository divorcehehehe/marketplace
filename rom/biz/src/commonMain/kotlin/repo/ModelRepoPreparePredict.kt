package rom.biz.repo

import rom.common.Context
import rom.common.models.State
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.repoPreparePredict(title: String) = worker {
    this.title = title
    description = """
        Предсказание при помощи модели
    """.trimIndent()
    on { state == State.RUNNING }
    handle {
        modelRepoPrepare = modelRepoRead.copy()

//        заглушка для обучения
        modelRepoPrepare.field = arrayOf(7.0, 8.0, 9.0)
    }
}
