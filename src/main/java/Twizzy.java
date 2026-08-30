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
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.println("added: " + command);
            }

            System.out.println(divider);
        }

        scanner.close();
    }
}
