package rom.stubs

import rom.common.models.Model
import rom.common.models.ModelId
import rom.stubs.ModelStubCase.MODEL_STUB_CASE

object ModelStub {
    fun get(): Model = MODEL_STUB_CASE.copy()

    fun prepareResult(block: Model.() -> Unit): Model = get().apply(block)

    fun prepareSearchList(filter: String) = listOf(
        Model(MODEL_STUB_CASE, "d-666-01", filter),
        Model(MODEL_STUB_CASE, "d-666-02", filter),
        Model(MODEL_STUB_CASE, "d-666-03", filter),
        Model(MODEL_STUB_CASE, "d-666-04", filter),
        Model(MODEL_STUB_CASE, "d-666-05", filter),
        Model(MODEL_STUB_CASE, "d-666-06", filter),
    )

    private fun Model(base: Model, id: String, filter: String) = base.copy(
        id = ModelId(id),
        name = "$filter $id",
    )
}
