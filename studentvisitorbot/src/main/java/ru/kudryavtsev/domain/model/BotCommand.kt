package ru.kudryavtsev.domain.model

import ru.kudryavtsev.command.BaseBotCommand

sealed interface BotCommand: BaseBotCommand {
    object VisitOp : BotCommand
    object VisitOop : BotCommand
    object Help: BotCommand
    object Info: BotCommand
    object UploadImage: BotCommand

    companion object: BaseBotCommand.CommandParser {
        private const val OP_VISIT_COMMAND = "/op"
        private const val OOP_VISIT_COMMAND = "/oop"
        private const val INFO_COMMAND = "/info"
        private const val HELP_COMMAND = "/help"
        private const val OP_IMAGE = "/image"

        override fun defineCommand(command: String): BaseBotCommand = when (command) {
            OP_VISIT_COMMAND -> VisitOp
            OOP_VISIT_COMMAND -> VisitOop
            HELP_COMMAND -> Help
            INFO_COMMAND -> Info
            OP_IMAGE -> UploadImage
            else -> super.defineCommand(command)
        }
    }
}
