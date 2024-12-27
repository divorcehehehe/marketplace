package rom.biz.repo

import rom.common.Context
import rom.common.models.State
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.repoPrepareTrain(title: String) = worker {
    this.title = title
    description = """
        Обучение модели
    """.trimIndent()
    on { state == State.RUNNING }
    handle {
        modelRepoPrepare = modelRepoRead.copy()

//        заглушка для обучения модели
        modelRepoPrepare.usVector = arrayOf(0.1, 0.2, 0.3)
        modelRepoPrepare.vtVector = arrayOf(0.4, 0.5, 0.6)
    }
}
