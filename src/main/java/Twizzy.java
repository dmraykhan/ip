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

            if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(command.substring(5)) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[taskIndex]);
            } else if (command.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(command.substring(7)) - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[taskIndex]);
            } else {
                try {
                    if (command.equals("todo") || command.startsWith("todo ")
                            && command.substring(5).trim().isEmpty()) {
                        throw new TwizzyException(
                                "A todo needs a description. Try: todo <description>");
                    } else if (command.startsWith("todo ")) {
                        tasks[taskCount] = new Todo(command.substring(5));
                    } else if (command.startsWith("deadline ")) {
                        int byIndex = command.indexOf(" /by ");
                        String description = command.substring(9, byIndex);
                        String by = command.substring(byIndex + 5);
                        tasks[taskCount] = new Deadline(description, by);
                    } else if (command.startsWith("event ")) {
                        int fromIndex = command.indexOf(" /from ");
                        int toIndex = command.indexOf(" /to ");
                        String description = command.substring(6, fromIndex);
                        String from = command.substring(fromIndex + 7, toIndex);
                        String to = command.substring(toIndex + 5);
                        tasks[taskCount] = new Event(description, from, to);
                    } else {
                        throw new TwizzyException(
                                "I don't recognize that command. Try todo, deadline, event, list, mark, unmark, or bye.");
                    }
                    taskCount++;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks[taskCount - 1]);
                    String taskWord = taskCount == 1 ? "task" : "tasks";
                    System.out.println("Now you have " + taskCount + " " + taskWord + " in the list.");
                } catch (TwizzyException exception) {
                    System.out.println("OOPS!!! " + exception.getMessage());
                }
            }

            System.out.println(divider);
        }

        scanner.close();
    }
}
