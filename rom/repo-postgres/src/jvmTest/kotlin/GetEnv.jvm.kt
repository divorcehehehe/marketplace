package rom.backend.repo.postgresql

actual fun getEnv(name: String): String? = System.getenv(name)
