import org.junit.jupiter.api.Test
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
}