import java.util.Scanner;

/**
 * Runs the Twizzy chatbot and manages its in-memory task list.
 */
public class Twizzy {
    /**
     * Reads commands from standard input until the user exits the chatbot.
     *
     * @param args command-line arguments; not used
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String divider = "____________________________________________________________";
        Task[] tasks = new Task[100];
        int taskCount = 0;
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

        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);

            if (command.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(divider);
                break;
            }

            try {
                if (command.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i]);
                    }
                } else if (command.equals("mark") || command.startsWith("mark ")) {
                    int taskIndex = parseTaskIndex(command, "mark", taskCount);
                    tasks[taskIndex].markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[taskIndex]);
                } else if (command.equals("unmark") || command.startsWith("unmark ")) {
                    int taskIndex = parseTaskIndex(command, "unmark", taskCount);
                    tasks[taskIndex].markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[taskIndex]);
                } else {
                    Task newTask = parseTask(command);
                    if (taskCount == tasks.length) {
                        throw new TwizzyException("The task list is full. Remove a task before adding another.");
                    }
                    tasks[taskCount] = newTask;
                    taskCount++;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + newTask);
                    String taskWord = taskCount == 1 ? "task" : "tasks";
                    System.out.println("Now you have " + taskCount + " " + taskWord + " in the list.");
                }
            } catch (TwizzyException exception) {
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
     * @return the task described by the command
     * @throws TwizzyException if the command or one of its fields is invalid
     */
    private static Task parseTask(String command) throws TwizzyException {
        if (command.trim().isEmpty()) {
            throw new TwizzyException("Please enter a command.");
        }

        if (command.equals("todo") || command.startsWith("todo ")) {
            String description = command.substring(4).trim();
            if (description.isEmpty()) {
                throw new TwizzyException("A todo needs a description. Try: todo <description>");
            }
            return new Todo(description);
        }

        if (command.equals("deadline") || command.startsWith("deadline ")) {
            String details = command.substring(8).trim();
            int byIndex = details.indexOf("/by");
            if (byIndex < 0) {
                throw new TwizzyException("A deadline needs /by followed by a time.");
            }
            String description = details.substring(0, byIndex).trim();
            String by = details.substring(byIndex + 3).trim();
            if (description.isEmpty()) {
                throw new TwizzyException(
                        "A deadline needs a description. Try: deadline <description> /by <time>");
            }
            if (by.isEmpty()) {
                throw new TwizzyException("The deadline time cannot be empty after /by.");
            }
            return new Deadline(description, by);
        }

        if (command.equals("event") || command.startsWith("event ")) {
            String details = command.substring(5).trim();
            int fromIndex = details.indexOf("/from");
            int toIndex = fromIndex < 0 ? -1 : details.indexOf("/to", fromIndex + 5);
            if (fromIndex < 0) {
                throw new TwizzyException("An event needs /from followed by a start time.");
            }
            if (toIndex < 0) {
                throw new TwizzyException("An event needs /to followed by an end time.");
            }
            String description = details.substring(0, fromIndex).trim();
            String from = details.substring(fromIndex + 5, toIndex).trim();
            String to = details.substring(toIndex + 3).trim();
            if (description.isEmpty()) {
                throw new TwizzyException(
                        "An event needs a description. Try: event <description> /from <start> /to <end>");
            }
            if (from.isEmpty()) {
                throw new TwizzyException("The event start time cannot be empty after /from.");
            }
            if (to.isEmpty()) {
                throw new TwizzyException("The event end time cannot be empty after /to.");
            }
            return new Event(description, from, to);
        }

        throw new TwizzyException(
                "I don't recognize that command. Try todo, deadline, event, list, mark, unmark, or bye.");
    }

    /**
     * Parses and validates the task number in a mark or unmark command.
     *
     * @param command complete user command
     * @param action command name, either mark or unmark
     * @param taskCount number of tasks currently stored
     * @return zero-based index of the selected task
     * @throws TwizzyException if the task number is missing, malformed, or out of range
     */
    private static int parseTaskIndex(String command, String action, int taskCount)
            throws TwizzyException {
        String numberText = command.substring(action.length()).trim();
        if (numberText.isEmpty()) {
            throw new TwizzyException("Please provide a task number. Try: " + action + " <number>");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException exception) {
            throw new TwizzyException("The task number must be a whole number.");
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            if (taskCount == 0) {
                throw new TwizzyException("There are no tasks to " + action + ".");
            }
            throw new TwizzyException("Choose a task number from 1 to " + taskCount + ".");
        }
        return taskNumber - 1;
    }
}
