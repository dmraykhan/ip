package twizzy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/** Tests command parsing and validation. */
class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void parseTask_validDeadline_createsDeadlineWithParsedDate() throws TwizzyException {
        Task task = parser.parseTask("deadline submit report /by 2026-09-30", CommandType.DEADLINE);

        Deadline deadline = assertInstanceOf(Deadline.class, task);
        assertEquals("submit report", deadline.getDescription());
        assertEquals(LocalDate.of(2026, 9, 30), deadline.getBy());
    }

    @Test
    void parseTask_validEvent_createsEventWithParsedDates() throws TwizzyException {
        Task task = parser.parseTask("event project meeting /from 2026-09-21 /to 2026-09-22",
                CommandType.EVENT);

        Event event = assertInstanceOf(Event.class, task);
        assertEquals("project meeting", event.getDescription());
        assertEquals(LocalDate.of(2026, 9, 21), event.getFrom());
        assertEquals(LocalDate.of(2026, 9, 22), event.getTo());
    }

    @Test
    void parseTask_invalidDate_throwsHelpfulException() {
        TwizzyException exception = assertThrows(TwizzyException.class,
                () -> parser.parseTask("deadline submit report /by 2026-02-29", CommandType.DEADLINE));

        assertEquals("The deadline date must be a valid date in yyyy-MM-dd format.",
                exception.getMessage());
    }

    @Test
    void parseTaskIndex_validOneBasedNumber_returnsZeroBasedIndex() throws TwizzyException {
        assertEquals(1, parser.parseTaskIndex("mark 2", CommandType.MARK, 3));
    }

    @Test
    void parseTaskIndex_outOfRangeNumber_throwsHelpfulException() {
        TwizzyException exception = assertThrows(TwizzyException.class,
                () -> parser.parseTaskIndex("delete 3", CommandType.DELETE, 2));

        assertEquals("Choose a task number from 1 to 2.", exception.getMessage());
    }
}
