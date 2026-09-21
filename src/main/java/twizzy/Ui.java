package twizzy;

import java.util.List;
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
        showLines(DIVIDER, BANNER, "Yo, I'm Twizzy — your task-list twin.",
                "Drop a command. I'll keep the chaos organized.", DIVIDER);
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
        System.out.println("I'm out. Your tasks aren't — don't ghost them.");
    }

    /** Displays a user-facing error. */
    public void showError(String message) {
        System.out.println("Yeah, no. " + message);
    }

    /** Displays all tasks in their current order. */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here's the current chaos:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /** Displays the tasks that match a user's search keyword. */
    public void showMatchingTasks(List<Task> matchingTasks) {
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            System.out.println((i + 1) + "." + matchingTasks.get(i));
        }
    }

    /** Displays confirmation that a task was added. */
    public void showTaskAdded(Task task, int taskCount) {
        showLines("Locked in. I added:", "  " + task);
        showTaskCount(taskCount);
    }

    /** Displays confirmation that a task was marked as completed. */
    public void showTaskMarked(Task task) {
        showLines("Huge. One less thing haunting you:", "  " + task);
    }

    /** Displays confirmation that a task was marked as incomplete. */
    public void showTaskUnmarked(Task task) {
        showLines("Plot twist. This one's back:", "  " + task);
    }

    /** Displays confirmation that a task was removed. */
    public void showTaskDeleted(Task task, int taskCount) {
        showLines("Gone. We never knew this task:", "  " + task);
        showTaskCount(taskCount);
    }

    /** Closes the standard-input scanner when Twizzy exits. */
    public void close() {
        scanner.close();
    }

    private void showTaskCount(int taskCount) {
        String taskWord = taskCount == 1 ? "task" : "tasks";
        System.out.println("You're juggling " + taskCount + " " + taskWord + " now.");
    }

    /**
     * Prints one or more complete output lines in the supplied order.
     *
     * @param lines lines to display
     */
    private void showLines(String... lines) {
        for (String line : lines) {
            System.out.println(line);
        }
    }
}
