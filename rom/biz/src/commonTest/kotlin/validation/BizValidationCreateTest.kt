package rom.biz.validation

import rom.common.models.Command
import kotlin.test.Test

class BizValidationCreateTest: BaseBizValidationTest() {
    override val command: Command = Command.CREATE

    @Test fun correctName() = validationNameCorrect(command, processor)
    @Test fun trimName() = validationNameTrim(command, processor)
    @Test fun emptyName() = validationNameEmpty(command, processor)
    @Test fun badSymbolsName() = validationNameSymbols(command, processor)

    @Test fun correctMacroPath() = validationMacroPathCorrect(command, processor)
    @Test fun trimMacroPath() = validationMacroPathTrim(command, processor)
    @Test fun emptyMacroPath() = validationMacroPathEmpty(command, processor)
    @Test fun badMacroPath() = validationMacroPathContent(command, processor)

    @Test fun correctSolverPath() = validationSolverPathCorrect(command, processor)
    @Test fun trimSolverPath() = validationSolverPathTrim(command, processor)
    @Test fun emptySolverPath() = validationSolverPathEmpty(command, processor)
    @Test fun badSolverPath() = validationSolverPathContent(command, processor)

    @Test fun correctParams() =        validationParamsCorrect(command, processor)
    @Test fun emptyParams() =          validationParamsEmpty(command, processor)
    @Test fun badParamLine() =         validationParamsBadLine(command, processor)
    @Test fun badParamPosition() =     validationParamsBadPosition(command, processor)
    @Test fun emptyParamSeparator() =  validationParamsEmptySeparator(command, processor)
    @Test fun emptyParamName() =       validationParamsEmptyName(command, processor)
    @Test fun badParamName() =         validationParamsBadName(command, processor)
    @Test fun emptyParamUnits() =      validationParamsEmptyUnits(command, processor)
    @Test fun badParamUnits() =        validationParamsBadUnits(command, processor)
    @Test fun badParamBoundsSize() =   validationParamsBadBoundsSize(command, processor)
    @Test fun badParamBoundsValues() = validationParamsBadBoundsValues(command, processor)

    @Test fun correctVisibility() = validationVisibilityCorrect(command, processor)
    @Test fun badVisibility() =     validationVisibilityEmpty(command, processor)
}
