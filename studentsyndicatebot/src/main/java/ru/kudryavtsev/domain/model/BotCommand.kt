package ru.kudryavtsev.domain.model

import ru.kudryavtsev.domain.command.BaseBotCommand

sealed interface BotCommand: BaseBotCommand {
    object Count: BotCommand
    object Help: BotCommand

    companion object: BaseBotCommand.CommandParser {
        private const val COUNT_COMMAND = "/count"
        private const val HELP_COMMAND = "/help"
        override fun defineCommand(command: String): BaseBotCommand = when (command) {
            COUNT_COMMAND -> Count
            HELP_COMMAND -> Help
            else -> super.defineCommand(command)
        }
    }
}
