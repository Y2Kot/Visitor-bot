package ru.kudryavtsev.command

interface BaseBotCommand {
    object Start : BaseBotCommand
    object Undefined : BaseBotCommand
    companion object {
        private const val START_COMMAND = "/start"
    }

    interface CommandParser {
        fun defineCommand(command: String): BaseBotCommand = when(command) {
            START_COMMAND -> Start
            else -> Undefined
        }
    }
}