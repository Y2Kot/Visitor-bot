package ru.kudryavtsev.domain.model

sealed interface BotCommand {
    object VisitOp : BotCommand
    object VisitOop : BotCommand
    object Start : BotCommand
    object Help: BotCommand
    object Info: BotCommand
    object UploadImage: BotCommand
    object Undefined : BotCommand

    companion object {
        private const val START_COMMAND = "/start"
        private const val OP_VISIT_COMMAND = "/op"
        private const val OOP_VISIT_COMMAND = "/oop"
        private const val INFO_COMMAND = "/info"
        private const val HELP_COMMAND = "/help"
        private const val OP_IMAGE = "/image"

        fun defineCommand(command: String): BotCommand = when (command) {
            START_COMMAND -> Start
            OP_VISIT_COMMAND -> VisitOp
            OOP_VISIT_COMMAND -> VisitOop
            HELP_COMMAND -> Help
            INFO_COMMAND -> Info
            OP_IMAGE -> UploadImage
            else -> Undefined
        }
    }
}
