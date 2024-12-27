package rom.backend.repo.postgresql

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject
import rom.common.models.Visibility

fun Table.visibilityEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.VISIBILITY_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.VISIBILITY_OWNER -> Visibility.VISIBLE_TO_OWNER
            SqlFields.VISIBILITY_PUBLIC -> Visibility.VISIBLE_PUBLIC
            else -> Visibility.NONE
        }
    },
    toDb = { value ->
        when (value) {
            Visibility.VISIBLE_TO_OWNER -> PgVisibilityOwner
            Visibility.VISIBLE_PUBLIC -> PgVisibilityPublic
            Visibility.NONE -> throw Exception("Wrong value of Visibility. NONE is unsupported")
        }
    }
)

sealed class PgVisibilityValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.VISIBILITY_TYPE
        value = eValue
    }
}

object PgVisibilityPublic: PgVisibilityValue(SqlFields.VISIBILITY_PUBLIC) {
    private fun readResolve(): Any = PgVisibilityPublic
}

object PgVisibilityOwner: PgVisibilityValue(SqlFields.VISIBILITY_OWNER) {
    private fun readResolve(): Any = PgVisibilityOwner
}
