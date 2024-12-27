package rom.backend.repo.postgresql

import rom.backend.repo.tests.*
import rom.common.repo.IRepoModel
import rom.repo.common.ModelRepoInitialized
import kotlin.test.AfterTest

private fun IRepoModel.clear() {
    val pgRepo = (this as ModelRepoInitialized).repo as RepoModelSql
    pgRepo.clear()
}

class RepoModelSQLCreateTest : RepoModelCreateTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoModelSQLReadTest : RepoModelReadTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoModelSQLUpdateTest : RepoModelUpdateTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(
        initObjects,
        randomUuid = { lockNew.asString() },
    )
    @AfterTest
    fun tearDown() {
        repo.clear()
    }
}

class RepoModelSQLDeleteTest : RepoModelDeleteTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}

class RepoModelSQLSearchTest : RepoModelSearchTest() {
    override val repo = SqlTestCompanion.repoUnderTestContainer(initObjects)
    @AfterTest
    fun tearDown() = repo.clear()
}
