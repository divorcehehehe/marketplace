package rom.common.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import rom.common.helpers.errorSystem
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class ModelRepoBase: IRepoModel {

    protected suspend fun tryModelMethod(
        timeout: Duration = 10.seconds,
        ctx: CoroutineContext = Dispatchers.IO,
        block: suspend () -> IDbModelResponse
    ) = try {
        withTimeout(timeout) {
            withContext(ctx) {
                block()
            }
        }
    } catch (e: Throwable) {
        DbModelResponseErr(errorSystem("methodException", e = e))
    }

    protected suspend fun tryModelsMethod(block: suspend () -> IDbModelsResponse) = try {
        block()
    } catch (e: Throwable) {
        DbModelsResponseErr(errorSystem("methodException", e = e))
    }

}
