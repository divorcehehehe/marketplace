package rom.biz.validation

import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail
import marketplace.cor.ICorChainDsl
import marketplace.cor.chain
import marketplace.cor.worker
import rom.common.models.Command
import rom.common.models.ModelPermissionClient
import rom.common.models.State
import rom.common.models.Visibility

fun ICorChainDsl<Context>.validatePermissions(title: String) = chain {
    this.title = title
    this.description = """
        Валидация прав допуска (по вертикали OR, по горизонтали AND):
        - требования для read/update/delete:
            - requestUserId == ownerId
            - visibility = Visibility.VISIBLE_PUBLIC && modelPermissions contains read/update/delete
        - требования для train/predict:
            - requestUserId == ownerId
            - visibility = Visibility.VISIBLE_PUBLIC && modelPermissions contains update
    """.trimIndent()
    on { state == State.RUNNING }
    worker {
        this.title = "Проверка read"
        this.description = this.title
        on {
            (
                command == Command.READ || command == Command.UPDATE ||
                command == Command.DELETE || command == Command.SEARCH ||
                command == Command.TRAIN || command == Command.PREDICT
            ) && (
                requestUserId != modelValidating.ownerId && (
                    modelValidating.visibility != Visibility.VISIBLE_PUBLIC ||
                    !modelValidating.permissionsClient.contains(ModelPermissionClient.READ)
                )
            )
        }
        handle {
            fail(
                errorValidation(
                    field = "permissionsClient",
                    violationCode = "badPermissions",
                    description = "You have no permissions to ${command.name} this model"
                )
            )
        }
    }
    worker {
        this.title = "Проверка update"
        this.description = this.title
        on {
            (
                command == Command.UPDATE || command == Command.DELETE ||
                command == Command.TRAIN || command == Command.PREDICT
            ) && (
                requestUserId != modelValidating.ownerId && (
                    modelValidating.visibility != Visibility.VISIBLE_PUBLIC ||
                    !modelValidating.permissionsClient.contains(ModelPermissionClient.UPDATE)
                )
            )
        }
        handle {
            fail(
                errorValidation(
                    field = "permissionsClient",
                    violationCode = "badPermissions",
                    description = "You have no permissions to ${command.name} this model"
                )
            )
        }
    }
    worker {
        this.title = "Проверка delete"
        this.description = this.title
        on {
            command == Command.DELETE && (
                requestUserId != modelValidating.ownerId && (
                    modelValidating.visibility != Visibility.VISIBLE_PUBLIC ||
                    !modelValidating.permissionsClient.contains(ModelPermissionClient.DELETE)
                )
            )
        }
        handle {
            fail(
                errorValidation(
                    field = "permissionsClient",
                    violationCode = "badPermissions",
                    description = "You have no permissions to delete this model"
                )
            )
        }
    }
}
