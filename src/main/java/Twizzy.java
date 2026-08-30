import java.util.Scanner;

public class Twizzy {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String divider = "____________________________________________________________";
        String[] tasks = new String[100];
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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = command;
                taskCount++;
                System.out.println("added: " + command);
            }

            System.out.println(divider);
        }

        scanner.close();
    }
}
