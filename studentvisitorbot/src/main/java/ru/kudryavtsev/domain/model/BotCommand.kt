package ru.kudryavtsev.domain.model

import ru.kudryavtsev.domain.command.BaseBotCommand

sealed interface BotCommand: BaseBotCommand {
    object VisitOpLecture : BotCommand
    object VisitOpLab : BotCommand
    object VisitOopLecture : BotCommand
    object VisitOopLab : BotCommand
    object Help: BotCommand
    object Info: BotCommand
    object UploadImage: BotCommand

    companion object: BaseBotCommand.CommandParser {
        private const val OP_VISIT_LECTURE_COMMAND = "/oplect"
        private const val OP_VISIT_LAB_COMMAND = "/oplab"
        private const val OOP_VISIT_LECTURE_COMMAND = "/ooplect"
        private const val OOP_VISIT_LAB_COMMAND = "/ooplab"
        private const val INFO_COMMAND = "/info"
        private const val HELP_COMMAND = "/help"
        private const val OP_IMAGE = "/image"

        override fun defineCommand(command: String): BaseBotCommand = when (command) {
            OP_VISIT_LECTURE_COMMAND -> VisitOpLecture
            OP_VISIT_LAB_COMMAND -> VisitOpLab
            OOP_VISIT_LECTURE_COMMAND -> VisitOopLecture
            OOP_VISIT_LAB_COMMAND -> VisitOopLab
            HELP_COMMAND -> Help
            INFO_COMMAND -> Info
            OP_IMAGE -> UploadImage
            else -> super.defineCommand(command)
        }
    }
}
