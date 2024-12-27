package rom.backend.repo.postgresql

data class SqlProperties(
    val host: String = "localhost",
    val port: Int = 5432,
    val user: String = "postgres",
    val password: String = "marketplace-pass",
    val database: String = "marketplace-models",
    val schema: String = "public",
    val table: String = "models",
) {
    val url: String
        get() = "jdbc:postgresql://${host}:${port}/${database}"
}
