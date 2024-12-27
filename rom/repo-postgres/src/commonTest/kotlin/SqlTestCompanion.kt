package rom.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import rom.common.models.Model
import rom.repo.common.ModelRepoInitialized

object SqlTestCompanion {
    private const val HOST = "localhost"
    private const val USER = "postgres"
    private const val PASS = "marketplace-pass"
    val PORT = getEnv("postgresPort")?.toIntOrNull() ?: 5432

    fun repoUnderTestContainer(
        initObjects: Collection<Model> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ) = ModelRepoInitialized(
        repo = RepoModelSql(
            SqlProperties(
                host = HOST,
                user = USER,
                password = PASS,
                port = PORT,
            ),
            randomUuid = randomUuid
        ),
        initObjects = initObjects,
    )
}
