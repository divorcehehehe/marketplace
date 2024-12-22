package rom.biz

import rom.common.Context
import rom.common.CorSettings
import rom.common.models.State
import rom.stubs.ModelStub

@Suppress("unused", "RedundantSuspendModifier")
class ModelProcessor(val corSettings: CorSettings) {
    suspend fun exec(ctx: Context) {
        ctx.modelResponse = ModelStub.get()
        ctx.modelsResponse = ModelStub.prepareSearchList("model search").toMutableList()
        ctx.state = State.RUNNING
    }
}
