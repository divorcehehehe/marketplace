package rom.biz

import marketplace.cor.rootChain
import marketplace.cor.worker
import rom.biz.general.initStatus
import rom.biz.general.operation
import rom.biz.general.stubs
import rom.biz.stubs.*
import rom.biz.validation.*
import rom.common.Context
import rom.common.CorSettings
import rom.common.models.Command
import rom.common.models.ModelId
import rom.common.models.ModelLock

class ModelProcessor(
    private val corSettings: CorSettings = CorSettings.NONE
) {
    suspend fun exec(ctx: Context) = businessChain.exec(ctx.also { it.corSettings = corSettings })
    private val businessChain = rootChain {
        initStatus("Инициализация статуса")
        operation("Создание модели", Command.CREATE) {
            stubs("Обработка стабов") {
                stubCreateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadName("Имитация ошибки валидации имени модели")
                stubValidationBadMacroPath("Имитация ошибки валидации пути к макросу")
                stubValidationBadSolverPath("Имитация ошибки валидации пути к солверу")
                stubValidationBadParamLine("Имитация ошибки валидации номера строки с параметром")
                stubValidationBadParamPosition("Имитация ошибки валидации позиции параметра в строке")
                stubValidationBadParamSeparator("Имитация ошибки валидации разделителя строки")
                stubValidationBadParamName("Имитация ошибки валидации имени параметра")
                stubValidationBadParamUnits("Имитация ошибки валидации единиц измерения параметра")
                stubValidationBadParamBounds("Имитация ошибки валидации границ диапозона варьирования параметра")
                stubValidationBadSampling("Имитация ошибки валидации способа сэмплирования модели")
                stubValidationBadVisibility("Имитация ошибки валидации видимости модели")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.deepCopy() }

                worker("Очистка id") { modelValidating.id = ModelId(modelValidating.id.asString().trim()) }
                worker("Очистка поля name") { modelValidating.name = modelValidating.name.trim() }
                worker("Очистка поля macroPath") { modelValidating.macroPath = modelValidating.macroPath.trim() }
                worker("Очистка поля solverPath") { modelValidating.solverPath = modelValidating.solverPath.trim() }

                validateNameNotEmpty("Проверка наличия поля name")
                validateNameHasContent(      "Проверка поля name")

                validateMacroPathNotEmpty("Проверка наличия поля macroPath")
                validateMacroPathHasContent(      "Проверка поля macroPath")

                validateSolverPathNotEmpty("Проверка наличия поля solverPath")
                validateSolverPathHasContent(      "Проверка поля solverPath")

                validateParamsNotEmpty("Проверка наличия поля params")
                validateParamsHasContent(      "Проверка поля params")

                validateVisibilityNotEmpty("Проверка наличия поля visibility")

                finishModelValidation("Завершение проверок")
            }
        }

        operation("Получить модель", Command.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationCannotRead("Имитация отсутствия прав")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.deepCopy() }

                worker("Очистка id") { modelValidating.id = ModelId(modelValidating.id.asString().trim()) }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                finishModelValidation("Завершение проверок")
            }
        }

        operation("Изменить модель", Command.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadLock("Имитация ошибки валидации блокировки")
                stubValidationBadName("Имитация ошибки валидации имени модели")
                stubValidationBadMacroPath("Имитация ошибки валидации пути к макросу")
                stubValidationBadSolverPath("Имитация ошибки валидации пути к солверу")
                stubValidationBadParamLine("Имитация ошибки валидации номера строки с параметром")
                stubValidationBadParamPosition("Имитация ошибки валидации позиции параметра в строке")
                stubValidationBadParamSeparator("Имитация ошибки валидации разделителя строки")
                stubValidationBadParamName("Имитация ошибки валидации имени параметра")
                stubValidationBadParamUnits("Имитация ошибки валидации единиц измерения параметра")
                stubValidationBadParamBounds("Имитация ошибки валидации границ диапозона варьирования параметра")
                stubValidationBadSampling("Имитация ошибки валидации способа сэмплирования модели")
                stubValidationBadVisibility("Имитация ошибки валидации видимости модели")
                stubValidationCannotRead("Имитация отсутствия прав")
                stubValidationCannotUpdate("Имитация отсутствия прав")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.deepCopy() }

                worker("Очистка id") { modelValidating.id = ModelId(modelValidating.id.asString().trim()) }
                worker("Очистка поля lock") { modelValidating.lock = ModelLock(modelValidating.lock.asString().trim()) }
                worker("Очистка поля name") { modelValidating.name = modelValidating.name.trim() }
                worker("Очистка поля macroPath") { modelValidating.macroPath = modelValidating.macroPath.trim() }
                worker("Очистка поля solverPath") { modelValidating.solverPath = modelValidating.solverPath.trim() }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateLockNotEmpty("Проверка наличия поля lock")
                validateLockProperFormat(    "Проверка поля lock")

                validateNameNotEmpty("Проверка наличия поля name")
                validateNameHasContent(      "Проверка поля name")

                validateMacroPathNotEmpty("Проверка наличия поля macroPath")
                validateMacroPathHasContent(      "Проверка поля macroPath")

                validateSolverPathNotEmpty("Проверка наличия поля solverPath")
                validateSolverPathHasContent(      "Проверка поля solverPath")

                validateParamsNotEmpty("Проверка наличия поля params")
                validateParamsHasContent(      "Проверка поля params")

                validateVisibilityNotEmpty("Проверка наличия поля visibility")

                finishModelValidation("Завершение проверок")
            }
        }

        operation("Удалить модель", Command.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadLock("Имитация ошибки валидации блокировки")
                stubValidationCannotRead("Имитация отсутствия прав")
                stubValidationCannotUpdate("Имитация отсутствия прав")
                stubValidationCannotDelete("Имитация отсутствия прав")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.deepCopy() }

                worker("Очистка id") { modelValidating.id = ModelId(modelValidating.id.asString().trim()) }
                worker("Очистка поля lock") { modelValidating.lock = ModelLock(modelValidating.lock.asString().trim()) }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateLockNotEmpty("Проверка наличия поля lock")
                validateLockProperFormat(    "Проверка поля lock")

                finishModelValidation("Завершение проверок")
            }
        }

        operation("Поиск моделей", Command.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadSearchString("Имитация ошибки валидации поисковой строки")
                stubValidationCannotRead("Имитация отсутствия прав")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelFilterValidating") {
                    modelFilterValidating = modelFilterRequest.deepCopy()
                }

                validateSearchStringLength("Валидация длины строки поиска в фильтре")
                finishModelFilterValidation("Завершение проверок")
            }
        }

        operation("Обучить модель", Command.TRAIN) {
            stubs("Обработка стабов") {
                stubTrainSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadLock("Имитация ошибки валидации блокировки")
                stubValidationCannotRead("Имитация отсутствия прав")
                stubValidationCannotUpdate("Имитация отсутствия прав")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.deepCopy() }

                worker("Очистка id") { modelValidating.id = ModelId(modelValidating.id.asString().trim()) }
                worker("Очистка поля lock") { modelValidating.lock = ModelLock(modelValidating.lock.asString().trim()) }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateLockNotEmpty("Проверка наличия поля lock")
                validateLockProperFormat(    "Проверка поля lock")

                finishModelValidation("Завершение проверок")
            }
        }

        operation("Предсказать при помощи модели", Command.PREDICT) {
            stubs("Обработка стабов") {
                stubPredictSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadLock("Имитация ошибки валидации блокировки")
                stubValidationBadParamValues("Имитация ошибки валидации значений параметров")
                stubValidationCannotRead("Имитация отсутствия прав")
                stubValidationCannotUpdate("Имитация отсутствия прав")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.deepCopy() }

                worker("Очистка id") { modelValidating.id = ModelId(modelValidating.id.asString().trim()) }
                worker("Очистка поля lock") { modelValidating.lock = ModelLock(modelValidating.lock.asString().trim()) }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateLockNotEmpty("Проверка наличия поля lock")
                validateLockProperFormat(    "Проверка поля lock")

                finishModelValidation("Завершение проверок")
            }
        }
    }.build()
}
