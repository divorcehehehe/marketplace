package rom.biz

import marketplace.cor.chain
import marketplace.cor.rootChain
import marketplace.cor.worker
import rom.biz.repo.*
import rom.biz.general.initStatus
import rom.biz.general.operation
import rom.biz.general.stubs
import rom.biz.stubs.*
import rom.biz.validation.*
import rom.common.Context
import rom.common.CorSettings
import rom.common.models.*

class ModelProcessor(
    private val corSettings: CorSettings = CorSettings.NONE
) {
    suspend fun exec(ctx: Context) = businessChain.exec(ctx.also { it.corSettings = corSettings })
    private val businessChain = rootChain {
        initStatus("Инициализация статуса")
        initRepo("Инициализация репозитория")

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
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.copy() }

                worker("Очистка id") { modelValidating.id = ModelId.NONE }
                worker("Очистка requestUserId") {
                    modelValidating.requestUserId = UserId(modelValidating.requestUserId.asString().trim())
                }
                worker("Очистка поля name") { modelValidating.name = modelValidating.name.trim() }
                worker("Очистка поля macroPath") { modelValidating.macroPath = modelValidating.macroPath.trim() }
                worker("Очистка поля solverPath") { modelValidating.solverPath = modelValidating.solverPath.trim() }

                validateRequestUserIdNotEmpty("Проверка наличия поля requestUserId")
                validateRequestUserIdProperFormat(    "Проверка поля requestUserId")

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

            chain {
                title = "Логика сохранения"
                repoPrepareCreate("Подготовка объекта для сохранения")
                repoCreate("Создание модели в БД")
            }

            prepareResult("Подготовка ответа")
        }

        operation("Получить модель", Command.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.copy() }

                worker("Очистка id") { modelValidating.id = ModelId(modelValidating.id.asString().trim()) }
                worker("Очистка requestUserId") {
                    modelValidating.requestUserId = UserId(modelValidating.requestUserId.asString().trim())
                }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateRequestUserIdNotEmpty("Проверка наличия поля requestUserId")
                validateRequestUserIdProperFormat(    "Проверка поля requestUserId")

                finishModelValidation("Завершение проверок")
            }

            chain {
                title = "Логика чтения"
                repoRead("Чтение модели из БД")
                worker {
                    title = "Подготовка ответа для Read"
                    on { state == State.RUNNING }
                    handle { modelRepoDone = modelRepoRead }
                }
            }

            prepareResult("Подготовка ответа")
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
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.copy() }

                worker("Очистка id") { modelValidating.id = ModelId(modelValidating.id.asString().trim()) }
                worker("Очистка requestUserId") {
                    modelValidating.requestUserId = UserId(modelValidating.requestUserId.asString().trim())
                }
                worker("Очистка поля lock") { modelValidating.lock = ModelLock(modelValidating.lock.asString().trim()) }
                worker("Очистка поля name") { modelValidating.name = modelValidating.name.trim() }
                worker("Очистка поля macroPath") { modelValidating.macroPath = modelValidating.macroPath.trim() }
                worker("Очистка поля solverPath") { modelValidating.solverPath = modelValidating.solverPath.trim() }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateRequestUserIdNotEmpty("Проверка наличия поля requestUserId")
                validateRequestUserIdProperFormat(    "Проверка поля requestUserId")

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

            chain {
                title = "Логика сохранения"
                repoRead("Чтение модели из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareUpdate("Подготовка объекта для обновления")
                repoUpdate("Обновление модели в БД")
            }

            prepareResult("Подготовка ответа")
        }

        operation("Удалить модель", Command.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadLock("Имитация ошибки валидации блокировки")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.copy() }

                worker("Очистка id") { modelValidating.id = ModelId(modelValidating.id.asString().trim()) }
                worker("Очистка requestUserId") {
                    modelValidating.requestUserId = UserId(modelValidating.requestUserId.asString().trim())
                }
                worker("Очистка поля lock") {
                    modelValidating.lock = ModelLock(modelValidating.lock.asString().trim())
                }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateRequestUserIdNotEmpty("Проверка наличия поля requestUserId")
                validateRequestUserIdProperFormat(    "Проверка поля requestUserId")

                validateLockNotEmpty("Проверка наличия поля lock")
                validateLockProperFormat(    "Проверка поля lock")

                finishModelValidation("Завершение проверок")
            }

            chain {
                title = "Логика удаления"
                repoRead("Чтение модели из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareDelete("Подготовка объекта для удаления")
                repoDelete("Удаление модели из БД")
            }

            prepareResult("Подготовка ответа")
        }

        operation("Поиск моделей", Command.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadSearchString("Имитация ошибки валидации поисковой строки")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelFilterValidating = modelFilterRequest.copy() }

                worker("Очистка requestUserId") {
                    modelFilterValidating.requestUserId = UserId(modelFilterValidating.requestUserId.asString().trim())
                }

                validateSearchRequestUserIdNotEmpty("Проверка наличия поля requestUserId")
                validateSearchRequestUserIdProperFormat(    "Проверка поля requestUserId")

                validateSearchStringLength("Валидация длины строки поиска в фильтре")

                finishModelFilterValidation("Завершение проверок")
            }

            repoSearch("Поиск модели в БД по фильтру")
            prepareResult("Подготовка ответа")
        }

        operation("Обучить модель", Command.TRAIN) {
            stubs("Обработка стабов") {
                stubTrainSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadLock("Имитация ошибки валидации блокировки")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.copy() }

                worker("Очистка id") { modelValidating.id = ModelId(modelValidating.id.asString().trim()) }
                worker("Очистка requestUserId") {
                    modelValidating.requestUserId = UserId(modelValidating.requestUserId.asString().trim())
                }
                worker("Очистка поля lock") {
                    modelValidating.lock = ModelLock(modelValidating.lock.asString().trim())
                }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateRequestUserIdNotEmpty("Проверка наличия поля requestUserId")
                validateRequestUserIdProperFormat(    "Проверка поля requestUserId")

                validateLockNotEmpty("Проверка наличия поля lock")
                validateLockProperFormat(    "Проверка поля lock")

                finishModelValidation("Завершение проверок")
            }

            chain {
                title = "Логика обучения"
                repoRead("Чтение модели из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                repoPrepareTrain("Обучение модели")
                repoUpdate("Посылаем обученную модель в БД")
            }

            prepareResult("Подготовка ответа")
        }

        operation("Предсказать при помощи модели", Command.PREDICT) {
            stubs("Обработка стабов") {
                stubPredictSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadLock("Имитация ошибки валидации блокировки")
                stubValidationBadParamValues("Имитация ошибки валидации значений параметров")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }

            validation {
                worker("Копируем поля в modelValidating") { modelValidating = modelRequest.copy() }

                worker("Очистка id") { modelValidating.id = ModelId(modelValidating.id.asString().trim()) }
                worker("Очистка requestUserId") {
                    modelValidating.requestUserId = UserId(modelValidating.requestUserId.asString().trim())
                }
                worker("Очистка поля lock") {
                    modelValidating.lock = ModelLock(modelValidating.lock.asString().trim())
                }

                validateIdNotEmpty("Проверка наличия поля id")
                validateIdProperFormat(    "Проверка поля id")

                validateRequestUserIdNotEmpty("Проверка наличия поля requestUserId")
                validateRequestUserIdProperFormat(    "Проверка поля requestUserId")

                validateLockNotEmpty("Проверка наличия поля lock")
                validateLockProperFormat(    "Проверка поля lock")

                validateParamValuesNotEmpty("Проверка paramValues")

                finishModelValidation("Завершение проверок")
            }

            chain {
                title = "Логика предсказания"
                repoRead("Чтение модели из БД")
                checkLock("Проверяем консистентность по оптимистичной блокировке")
                checkUSVTNotEmpty("Проверяем usVector и vtVector")
                checkParamValues("Проверяем ParamValues")
                repoPreparePredict("Обучение модели")
                repoUpdate("Посылаем медель с предсказанием в БД")
            }

            prepareResult("Подготовка ответа")
        }
    }.build()
}
