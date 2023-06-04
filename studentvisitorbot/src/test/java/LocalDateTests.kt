import org.junit.jupiter.api.Test
import ru.kudryavtsev.domain.util.currentStudyingWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals

class LocalDateTests {
    @Test
    fun `checking LocalDate parsing`() {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val samples = listOf(
            "10.12.2022"
        )
        samples.forEach { data ->
            assertEquals(LocalDate.of(2022, 12, 10), LocalDate.parse(data, formatter))
        }
    }

    @Test
    fun `checking week number from LocalDate`() {
        val validationMap = mapOf(
            2L to LocalDate.of(2023, 2, 14),
            3L to LocalDate.of(2023, 2, 21),
            4L to LocalDate.of(2023, 2, 27),
            5L to LocalDate.of(2023, 3, 6),
            6L to LocalDate.of(2023, 3, 13),
            7L to LocalDate.of(2023, 3, 21),
            8L to LocalDate.of(2023, 3, 28),
            9L to LocalDate.of(2023, 4, 6),
            10L to LocalDate.of(2023, 4, 10),
            12L to LocalDate.of(2023, 4, 25),
            14L to LocalDate.of(2023, 5, 9),
            13L to LocalDate.of(2023, 5, 2),
            17L to LocalDate.of(2023, 6, 4),


        )

        validationMap.forEach { (week, date) ->
            assertEquals(week, date.currentStudyingWeek())
        }
    }
}
