import org.junit.jupiter.api.Test
import ru.kudryavtsev.domain.model.StudyWeek
import ru.kudryavtsev.domain.util.contains
import ru.kudryavtsev.domain.util.weeksBetween
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocalDateTests {
    private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    @Test
    fun `checking LocalDate parsing`() {
        val samples = listOf(
            "10.12.2022"
        )
        samples.forEach { date ->
            1 in 2..3
            assertEquals(LocalDate.of(2022, 12, 10), LocalDate.parse(date, formatter))
        }
    }

    @Test
    fun `check if day in week`() {
        val week = StudyWeek(
            from = LocalDate.of(2023, 3, 6),
            to = LocalDate.of(2023, 3, 12)
        )
        val samples = listOf(
            "06.03.2023", "09.03.2023", "11.03.2023", "12.03.2023"
        )

        samples.forEach { date ->
            assertTrue(LocalDate.parse(date, formatter) in week)
        }
    }

    @Test
    fun `check if day not in week`() {
        val week = StudyWeek(
            from = LocalDate.of(2023, 3, 6),
            to = LocalDate.of(2023, 3, 12)
        )
        val samples = listOf(
            "05.03.2023", "09.03.2022", "11.04.2023", "13.03.2023"
        )

        samples.forEach { date ->
            assertFalse(LocalDate.parse(date, formatter) in week)
        }
    }

    @Test
    fun `check weeks count`() {
        val startDate = LocalDate.of(2023, 2, 6)
        val endDate = LocalDate.of(2023, 3, 12)
        assertEquals(5, startDate.weeksBetween(endDate))
    }
}
