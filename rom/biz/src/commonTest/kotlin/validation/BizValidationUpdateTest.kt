package rom.biz.validation

import rom.common.models.Command
import kotlin.test.Test

class BizValidationUpdateTest: BaseBizValidationTest() {
    override val command = Command.UPDATE

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)

    @Test fun correctRequestUserId() = validationRequestUserIdCorrect(command, processor)
    @Test fun trimRequestUserId() = validationRequestUserIdTrim(command, processor)
    @Test fun emptyRequestUserId() = validationRequestUserIdEmpty(command, processor)
    @Test fun badFormatRequestUserId() = validationRequestUserIdFormat(command, processor)

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
