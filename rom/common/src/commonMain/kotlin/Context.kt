package rom.common

import kotlinx.datetime.Instant
import rom.common.models.*
import rom.common.stubs.Stubs

data class Context(
    var command: Command = Command.NONE,
    var state: State = State.NONE,
    val errors: MutableList<ROMError> = mutableListOf(),
    var corSettings: CorSettings = CorSettings(),
    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: Stubs = Stubs.NONE,
    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var requestUserId: UserId = UserId.NONE,

    var modelRequest: Model = Model(),
    var modelFilterRequest: ModelFilter = ModelFilter(),

    var modelValidating: Model = Model(),
    var modelFilterValidating: ModelFilter = ModelFilter(),

    var modelValidated: Model = Model(),
    var modelFilterValidated: ModelFilter = ModelFilter(),

    var modelResponse: Model = Model(),
    var modelsResponse: MutableList<Model> = mutableListOf(),
) {

    override fun hashCode(): Int {
        return this::class.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Context

        if (command != other.command) return false
        if (state != other.state) return false
        if (errors != other.errors) return false
        if (workMode != other.workMode) return false
        if (stubCase != other.stubCase) return false
        if (requestId != other.requestId) return false
        if (timeStart != other.timeStart) return false
        if (modelRequest != other.modelRequest) return false
        if (modelFilterRequest != other.modelFilterRequest) return false
        if (modelValidating != other.modelValidating) return false
        if (modelFilterValidating != other.modelFilterValidating) return false
        if (modelValidated != other.modelValidated) return false
        if (modelFilterValidated != other.modelFilterValidated) return false
        if (modelResponse != other.modelResponse) return false
        if (modelsResponse != other.modelsResponse) return false

        return true
    }
}
