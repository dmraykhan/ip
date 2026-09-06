import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs the Twizzy chatbot and manages its in-memory task list.
 */
public class Twizzy {
    private static final Path DATA_FILE_PATH = Path.of("data", "twizzy.txt");
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    /**
     * Reads commands from standard input until the user exits the chatbot.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String divider = "____________________________________________________________";
        Storage storage = new Storage(DATA_FILE_PATH);
        ArrayList<Task> tasks;
        String banner = " _______        _                     \n"
                + "|__   __|      (_)                    \n"
                + "   | |_      ___ __________   _       \n"
                + "   | \\ \\ /\\ / / |_  /_  / | | |      \n"
                + "   | |\\ V  V /| |/ / / /| |_| |      \n"
                + "   |_| \\_/\\_/ |_/___/___| \\__, |      \n"
                + "                           __/ |      \n"
                + "                          |___/";

        System.out.println(divider);
        System.out.println(banner);
        System.out.println("Hello! I'm Twizzy.");
        System.out.println("What can I do for you?");
        System.out.println(divider);

        try {
            tasks = storage.load();
        } catch (IOException exception) {
            tasks = new ArrayList<>();
            System.out.println("OOPS!!! I couldn't load saved tasks: " + exception.getMessage());
            System.out.println(divider);
        }

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            CommandType commandType = CommandType.from(command);
            System.out.println(divider);

            if (commandType == CommandType.BYE) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                break;
            }

            try {
                switch (commandType) {
                case LIST:
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }
                    break;
                case MARK: {
                    int taskIndex = parseTaskIndex(command, commandType, tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    storage.save(tasks);
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(taskIndex));
                    break;
                }
                case UNMARK: {
                    int taskIndex = parseTaskIndex(command, commandType, tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    storage.save(tasks);
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks.get(taskIndex));
                    break;
                }
                case DELETE: {
                    int taskIndex = parseTaskIndex(command, commandType, tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    storage.save(tasks);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    String taskWord = tasks.size() == 1 ? "task" : "tasks";
                    System.out.println("Now you have " + tasks.size() + " " + taskWord + " in the list.");
                    break;
                }
                case TODO:
                case DEADLINE:
                case EVENT:
                case UNKNOWN: {
                    Task newTask = parseTask(command, commandType);
                    tasks.add(newTask);
                    storage.save(tasks);
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + newTask);
                    String taskWord = tasks.size() == 1 ? "task" : "tasks";
                    System.out.println("Now you have " + tasks.size() + " " + taskWord + " in the list.");
                    break;
                }
                case BYE:
                    break;
                }
            } catch (TwizzyException | IOException exception) {
                System.out.println("OOPS!!! " + exception.getMessage());
            }

            System.out.println(divider);
        }

        scanner.close();
    }

    /**
     * Parses and validates a command that creates a task.
     *
     * @param command complete user command
     * @param commandType identified command type
     * @return the task described by the command
     * @throws TwizzyException if the command or one of its fields is invalid
     */
    private static Task parseTask(String command, CommandType commandType) throws TwizzyException {
        if (command.trim().isEmpty()) {
            throw new TwizzyException("Please enter a command.");
        }

        if (commandType == CommandType.TODO) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new TwizzyException("A todo needs a description. Try: todo <description>");
            }
            return new Todo(description);
        }

        if (commandType == CommandType.DEADLINE) {
            String details = command.substring(8).trim();
            int byIndex = findCommandMarker(details, "/by", 0);
            if (byIndex < 0) {
                throw new TwizzyException("A deadline needs /by followed by a date.");
            }
            String description = details.substring(0, byIndex).trim();
            String by = details.substring(byIndex + 3).trim();
            if (description.isEmpty()) {
                throw new TwizzyException(
                        "A deadline needs a description. Try: deadline <description> /by <date>");
            }
            if (by.isEmpty()) {
                throw new TwizzyException("The deadline date cannot be empty after /by.");
            }
            return new Deadline(description, parseDate(by, "deadline"));
        }

        if (commandType == CommandType.EVENT) {
            String details = command.substring(5).trim();
            int fromIndex = findCommandMarker(details, "/from", 0);
            int toIndex = fromIndex < 0
                    ? -1
                    : findCommandMarker(details, "/to", fromIndex + 5);
            if (fromIndex < 0) {
                throw new TwizzyException("An event needs /from followed by a start date.");
            }
            if (toIndex < 0) {
                throw new TwizzyException("An event needs /to followed by an end date.");
            }
            String description = details.substring(0, fromIndex).trim();
            String from = details.substring(fromIndex + 5, toIndex).trim();
            String to = details.substring(toIndex + 3).trim();
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
            return new Event(description, parseDate(from, "event start"),
                    parseDate(to, "event end"));
        }

        throw new TwizzyException(
                "I don't recognize that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
    }

    /**
     * Parses a date in Twizzy's required ISO input format.
     *
     * @param dateText date supplied by the user
     * @param fieldName field name used in an error message
     * @return parsed date
     * @throws TwizzyException if the date is not a real date in yyyy-MM-dd format
     */
    private static LocalDate parseDate(String dateText, String fieldName) throws TwizzyException {
        try {
            return LocalDate.parse(dateText, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            throw new TwizzyException(
                    "The " + fieldName + " date must be a valid date in yyyy-MM-dd format.");
        }
    }

    /**
     * Finds a command marker that is separated from surrounding text by whitespace.
     * This prevents text such as {@code /bypass} from being mistaken for {@code /by}.
     *
     * @param text text containing command details
     * @param marker marker to find, including its leading slash
     * @param startIndex index at which to start searching
     * @return index of the marker, or -1 if no complete marker token exists
     */
    private static int findCommandMarker(String text, String marker, int startIndex) {
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

    /**
     * Parses and validates the task number in a command that selects one task.
     *
     * @param command complete user command
     * @param action command type, such as mark, unmark, or delete
     * @param taskCount number of tasks currently stored
     * @return zero-based index of the selected task
     * @throws TwizzyException if the task number is missing, malformed, or out of range
     */
    private static int parseTaskIndex(String command, CommandType action, int taskCount)
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
}
