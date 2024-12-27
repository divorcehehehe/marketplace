package rom.backend.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import rom.common.models.Sampling

fun Table.samplingEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.SAMPLING_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.SAMPLING_ADAPTIVE_SAMPLING -> Sampling.ADAPTIVE_SAMPLING
            SqlFields.SAMPLING_LATIN_HYPER_CUBE -> Sampling.LATIN_HYPER_CUBE
            else -> Sampling.NONE
        }
    },
    toDb = { value ->
        when (value) {
            Sampling.ADAPTIVE_SAMPLING -> PgSamplingAdaptive
            Sampling.LATIN_HYPER_CUBE -> PgSamplingLHS
            Sampling.NONE -> throw Exception("Wrong value of Sampling. NONE is unsupported")
        }
    }
)

sealed class PgSamplingValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.SAMPLING_TYPE
        value = eValue
    }
}

object PgSamplingAdaptive: PgSamplingValue(SqlFields.SAMPLING_ADAPTIVE_SAMPLING) {
    private fun readResolve(): Any = PgSamplingAdaptive
}

object PgSamplingLHS: PgSamplingValue(SqlFields.SAMPLING_LATIN_HYPER_CUBE) {
    private fun readResolve(): Any = PgSamplingLHS
}
