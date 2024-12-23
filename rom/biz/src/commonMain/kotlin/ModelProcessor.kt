package rom.biz

import rom.biz.general.initStatus
import rom.biz.general.operation
import rom.biz.general.stubs
import rom.biz.stubs.*
import rom.common.Context
import rom.common.CorSettings
import rom.common.models.Command
import marketplace.cor.rootChain

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
        }

        operation("Получить модель", Command.READ) {
            stubs("Обработка стабов") {
                stubReadSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }

        operation("Изменить модель", Command.UPDATE) {
            stubs("Обработка стабов") {
                stubUpdateSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
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
        }

        operation("Удалить модель", Command.DELETE) {
            stubs("Обработка стабов") {
                stubDeleteSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }

        operation("Поиск моделей", Command.SEARCH) {
            stubs("Обработка стабов") {
                stubSearchSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadSearchString("Имитация ошибки валидации поисковой строки")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }

        operation("Обучить модель", Command.TRAIN) {
            stubs("Обработка стабов") {
                stubTrainSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }

        operation("Предсказать при помощи модели", Command.PREDICT) {
            stubs("Обработка стабов") {
                stubPredictSuccess("Имитация успешной обработки", corSettings)
                stubValidationBadId("Имитация ошибки валидации id")
                stubValidationBadParamValues("Имитация ошибки валидации значений параметров")
                stubDbError("Имитация ошибки работы с БД")
                stubNoCase("Ошибка: запрошенный стаб недопустим")
            }
        }
    }.build()
}
