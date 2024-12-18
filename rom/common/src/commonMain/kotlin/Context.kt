package rom.common

import kotlinx.datetime.Instant
import rom.common.models.*
import rom.common.stubs.Stubs

data class Context(
    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val errors: MutableList<ROMError> = mutableListOf(),
    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,
    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var modelRequest: Model = Model(),
    var modelFilterRequest: ModelFilter = ModelFilter(),
    var modelResponse: Model = Model(),
    var modelsResponse: MutableList<Model> = mutableListOf(),
)
