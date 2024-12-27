package rom.backend.repo.postgresql

object SqlFields {
    const val ID = "id"
    const val LOCK = "lock"
    const val OWNER_ID = "owner_id"
    const val NAME = "name"
    const val MACRO_PATH = "macro_path"
    const val SOLVER_PATH = "solver_path"
    const val PARAMS = "params"
    const val US_VECTOR = "us_vector"
    const val VT_VECTOR = "vt_vector"
    const val SAMPLING = "sampling"
    const val VISIBILITY = "visibility"

    const val VISIBILITY_TYPE = "model_visibility_type"
    const val VISIBILITY_PUBLIC = "public"
    const val VISIBILITY_OWNER = "owner"

    const val SAMPLING_TYPE = "model_sampling_type"
    const val SAMPLING_ADAPTIVE_SAMPLING = "adaptive_sampling"
    const val SAMPLING_LATIN_HYPER_CUBE = "latin_hyper_cube"
}
