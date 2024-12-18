package rom.common.exceptions

import rom.common.models.Command

class UnknownCommand(command: Command) : Throwable("Wrong command $command at mapping toTransport stage")
