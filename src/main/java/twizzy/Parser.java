package twizzy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/** Interprets and validates commands entered by the user. */
public class Parser {
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private static final String BY_MARKER = "/by";

    private static final String FROM_MARKER = "/from";

    private static final String TO_MARKER = "/to";

    /** Creates a task from a complete add-task command. */
    public Task parseTask(String command, CommandType commandType) throws TwizzyException {
        if (command.trim().isEmpty()) {
            throw new TwizzyException("Please enter a command.");
        }
        if (commandType == CommandType.TODO) {
            String description = command.substring(CommandType.TODO.getKeyword().length()).trim();
            if (description.isEmpty()) {
                throw new TwizzyException("A todo needs a description. Try: todo <description>");
            }
            return new Todo(description);
        }
        if (commandType == CommandType.DEADLINE) {
            return parseDeadline(command);
        }
        if (commandType == CommandType.EVENT) {
            return parseEvent(command);
        }
        throw new TwizzyException(
                "I don't recognize that command. Try todo, deadline, event, find, list, mark, unmark, delete, or bye.");
    }

    /** Parses the task number in a command that selects one task. */
    public int parseTaskIndex(String command, CommandType action, int taskCount)
            throws TwizzyException {
        String actionKeyword = action.getKeyword();
        String numberText = command.substring(actionKeyword.length()).trim();
        if (numberText.isEmpty()) {
            throw new TwizzyException(
                    "Please provide a task number. Try: " + actionKeyword + " <number>");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException exception) {
            throw new TwizzyException("The task number must be a whole number.");
        }
        if (taskNumber < 1 || taskNumber > taskCount) {
            if (taskCount == 0) {
                throw new TwizzyException("There are no tasks to " + actionKeyword + ".");
            }
            throw new TwizzyException("Choose a task number from 1 to " + taskCount + ".");
        }
        return taskNumber - 1;
    }

    /** Parses and validates the keyword supplied to the find command. */
    public String parseFindKeyword(String command) throws TwizzyException {
        String keyword = command.substring(CommandType.FIND.getKeyword().length()).trim();
        if (keyword.isEmpty()) {
            throw new TwizzyException("Please provide a keyword. Try: find <keyword>");
        }
        return keyword;
    }

    private Task parseDeadline(String command) throws TwizzyException {
        String details = command.substring(CommandType.DEADLINE.getKeyword().length()).trim();
        int byIndex = findCommandMarker(details, BY_MARKER, 0);
        if (byIndex < 0) {
            throw new TwizzyException("A deadline needs /by followed by a date.");
        }
        String description = details.substring(0, byIndex).trim();
        String by = details.substring(byIndex + BY_MARKER.length()).trim();
        if (description.isEmpty()) {
            throw new TwizzyException(
                    "A deadline needs a description. Try: deadline <description> /by <date>");
        }
        if (by.isEmpty()) {
            throw new TwizzyException("The deadline date cannot be empty after /by.");
        }
        return new Deadline(description, parseDate(by, "deadline"));
    }

    private Task parseEvent(String command) throws TwizzyException {
        String details = command.substring(CommandType.EVENT.getKeyword().length()).trim();
        int fromIndex = findCommandMarker(details, FROM_MARKER, 0);
        int toIndex = fromIndex < 0 ? -1
                : findCommandMarker(details, TO_MARKER, fromIndex + FROM_MARKER.length());
        if (fromIndex < 0) {
            throw new TwizzyException("An event needs /from followed by a start date.");
        }
        if (toIndex < 0) {
            throw new TwizzyException("An event needs /to followed by an end date.");
        }
        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + FROM_MARKER.length(), toIndex).trim();
        String to = details.substring(toIndex + TO_MARKER.length()).trim();
        if (description.isEmpty()) {
            throw new TwizzyException(
                    "An event needs a description. Try: event <description> /from <start-date> /to <end-date>");
        }
        if (from.isEmpty()) {
            throw new TwizzyException("The event start date cannot be empty after /from.");
        }
        if (to.isEmpty()) {
            throw new TwizzyException("The event end date cannot be empty after /to.");
        }
        return new Event(description, parseDate(from, "event start"), parseDate(to, "event end"));
    }

    private LocalDate parseDate(String dateText, String fieldName) throws TwizzyException {
        try {
            return LocalDate.parse(dateText, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new TwizzyException(
                    "The " + fieldName + " date must be a valid date in yyyy-MM-dd format.");
        }
    }

    private int findCommandMarker(String text, String marker, int startIndex) {
        int markerIndex = text.indexOf(marker, startIndex);
        while (markerIndex >= 0) {
            int afterMarker = markerIndex + marker.length();
            boolean hasLeftBoundary = markerIndex == 0
                    || Character.isWhitespace(text.charAt(markerIndex - 1));
            boolean hasRightBoundary = afterMarker == text.length()
                    || Character.isWhitespace(text.charAt(afterMarker));
            if (hasLeftBoundary && hasRightBoundary) {
                return markerIndex;
            }
            markerIndex = text.indexOf(marker, markerIndex + 1);
        }
        return -1;
    }
}
