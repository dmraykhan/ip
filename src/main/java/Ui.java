import java.util.Scanner;

/** Handles console input and output for Twizzy. */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final String BANNER = " _______        _                     \n"
            + "|__   __|      (_)                    \n"
            + "   | |_      ___ __________   _       \n"
            + "   | \\ \\ /\\ / / |_  /_  / | | |      \n"
            + "   | |\\ V  V /| |/ / / /| |_| |      \n"
            + "   |_| \\_/\\_/ |_/___/___| \\__, |      \n"
            + "                           __/ |      \n"
            + "                          |___/";

    private final Scanner scanner = new Scanner(System.in);

    /** Displays Twizzy's banner and greeting. */
    public void showWelcome() {
        System.out.println(DIVIDER);
        System.out.println(BANNER);
        System.out.println("Hello! I'm Twizzy.");
        System.out.println("What can I do for you?");
        System.out.println(DIVIDER);
    }

    /** Returns whether another command is available from standard input. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Returns the next complete command from standard input. */
    public String readCommand() {
        return scanner.nextLine();
    }

    /** Displays the standard divider between user interactions. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /** Displays Twizzy's farewell message. */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /** Displays a user-facing error. */
    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
    }

    /** Displays all tasks in their current order. */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /** Displays confirmation that a task was added. */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /** Displays confirmation that a task was marked as completed. */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /** Displays confirmation that a task was marked as incomplete. */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /** Displays confirmation that a task was removed. */
    public void showTaskDeleted(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        showTaskCount(taskCount);
    }

    /** Closes the standard-input scanner when Twizzy exits. */
    public void close() {
        scanner.close();
    }

    private void showTaskCount(int taskCount) {
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println("Now you have " + taskCount + " " + taskWord + " in the list.");
    }
}
