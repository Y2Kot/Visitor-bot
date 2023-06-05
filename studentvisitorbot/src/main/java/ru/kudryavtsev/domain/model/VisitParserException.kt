package ru.kudryavtsev.domain.model

sealed class VisitParserException(message: String) : RuntimeException(message) {
    object NullInputData : VisitParserException(NULL_INPUT)
    object InvalidInputData : VisitParserException(INPUT_INVALID)
    object InvalidDateFormat : VisitParserException(INVALID_DATE_FORMAT)
    object InvalidPhotoNumber : VisitParserException(INVALID_NUMBER_FORMAT)

    companion object {
        private const val NULL_INPUT = "Отсутствует текст сообщения"
        private const val INPUT_INVALID = "Неверный формат входящего сообщения"
        private const val INVALID_DATE_FORMAT = "Неверный формат даты"
        private const val INVALID_NUMBER_FORMAT = "Ошибка при обработке значения номера на фото"
    }
}
