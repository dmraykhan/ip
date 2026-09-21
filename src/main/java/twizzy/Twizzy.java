package twizzy;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/** Coordinates Twizzy's UI, parser, task list, and storage components. */
public class Twizzy {
    private static final Path DATA_FILE_PATH = Path.of("data", "twizzy.txt");

    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d uuuu", Locale.ENGLISH);

    private final Parser parser;

    private final Storage storage;

    private final Ui ui;

    private TaskList tasks;

    /** Creates a Twizzy application using its default local data file. */
    public Twizzy() {
        parser = new Parser();
        storage = new Storage(DATA_FILE_PATH);
        ui = new Ui();
    }

    /** Starts the Twizzy application. */
    public static void main(String[] args) {
        new Twizzy().run();
    }

    /** Reads and executes commands until the user exits or input ends. */
    public void run() {
        ui.showWelcome();
        loadTasks();

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            CommandType commandType = CommandType.from(command);
            ui.showDivider();
            if (commandType == CommandType.BYE) {
                ui.showGoodbye();
                ui.showDivider();
                break;
            }
            try {
                execute(command, commandType);
            } catch (TwizzyException | IOException exception) {
                ui.showError(exception.getMessage());
            }
            ui.showDivider();
        }
        ui.close();
    }

    /**
     * Processes one command and returns the response for a graphical user interface.
     *
     * @param command complete command entered by the user
     * @return user-facing response for the command
     */
    public String getResponse(String command) {
        CommandType commandType = CommandType.from(command);
        try {
            return switch (commandType) {
            case BYE -> "I'm out. Your tasks aren't — don't ghost them.";
            case HELP -> helpText();
            case LIST -> command.equals("list snoozed")
                    ? formatSnoozedTaskList(tasks.getSnoozedTasks(LocalDate.now()))
                    : formatTaskList(tasks.getActiveTasks(LocalDate.now()), "Here's the current chaos:");
            case FIND -> formatTaskList(tasks.findActiveTasks(parser.parseFindKeyword(command), LocalDate.now()),
                    "Here are the matching tasks in your list:");
            case MARK -> formatStatusChange(command, commandType, true);
            case UNMARK -> formatStatusChange(command, commandType, false);
            case DELETE -> formatDeletion(command, commandType);
            case SNOOZE -> formatSnooze(command);
            case TODO, DEADLINE, EVENT, UNKNOWN -> formatAddition(command, commandType);
            };
        } catch (TwizzyException | IOException exception) {
            return "Yeah, no. " + exception.getMessage();
        }
    }

    /** Loads saved tasks for a graphical user interface without printing to standard output. */
    public void initializeForGui() {
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException exception) {
            tasks = new TaskList(List.of());
        }
    }

    private void loadTasks() {
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException exception) {
            tasks = new TaskList(List.of());
            ui.showError("I couldn't load saved tasks: " + exception.getMessage());
            ui.showDivider();
        }
    }

    private String formatTaskList(List<Task> displayedTasks, String heading) {
        StringBuilder response = new StringBuilder(heading);
        for (int i = 0; i < displayedTasks.size(); i++) {
            response.append(System.lineSeparator())
                    .append(i + 1)
                    .append('.')
                    .append(displayedTasks.get(i));
        }
        return response.toString();
    }

    private String helpText() {
        return "Commands:" + System.lineSeparator()
                + "  todo <description>" + System.lineSeparator()
                + "  deadline <description> /by <yyyy-MM-dd>" + System.lineSeparator()
                + "  event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>" + System.lineSeparator()
                + "  list | list snoozed | find <keyword>" + System.lineSeparator()
                + "  mark <number> | unmark <number> | delete <number>" + System.lineSeparator()
                + "  snooze <number> /until <yyyy-MM-dd>" + System.lineSeparator()
                + "  bye";
    }

    private String formatSnoozedTaskList(List<Task> snoozedTasks) {
        StringBuilder response = new StringBuilder("Here are the snoozed tasks:");
        for (int i = 0; i < snoozedTasks.size(); i++) {
            Task task = snoozedTasks.get(i);
            response.append(System.lineSeparator())
                    .append(i + 1)
                    .append('.')
                    .append(task)
                    .append(" (snoozed until: ")
                    .append(task.getSnoozedUntil().format(DISPLAY_DATE_FORMAT))
                    .append(')');
        }
        return response.toString();
    }

    private String formatAddition(String command, CommandType commandType)
            throws TwizzyException, IOException {
        Task task = parser.parseTask(command, commandType);
        tasks.add(task);
        storage.save(tasks.asList());
        return "Locked in. I added:" + System.lineSeparator()
                + "  " + task + System.lineSeparator()
                + formatTaskCount();
    }

    private String formatStatusChange(String command, CommandType commandType, boolean isDone)
            throws TwizzyException, IOException {
        LocalDate today = LocalDate.now();
        int taskIndex = parser.parseTaskIndex(command, commandType, tasks.getActiveTasks(today).size());
        Task task = tasks.getActive(taskIndex, today);
        if (task.isDone() == isDone) {
            String status = isDone ? "done" : "not done";
            throw new TwizzyException("That task is already marked as " + status + ".");
        }
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        storage.save(tasks.asList());
        String message = isDone ? "Huge. One less thing haunting you:" : "Plot twist. This one's back:";
        return message + System.lineSeparator() + "  " + task;
    }

    private String formatDeletion(String command, CommandType commandType)
            throws TwizzyException, IOException {
        LocalDate today = LocalDate.now();
        int taskIndex = parser.parseTaskIndex(command, commandType, tasks.getActiveTasks(today).size());
        Task removedTask = tasks.removeActive(taskIndex, today);
        storage.save(tasks.asList());
        return "Gone. We never knew this task:" + System.lineSeparator()
                + "  " + removedTask + System.lineSeparator()
                + formatTaskCount();
    }

    private String formatSnooze(String command) throws TwizzyException, IOException {
        LocalDate today = LocalDate.now();
        Parser.SnoozeDetails details = parser.parseSnooze(command, tasks.getActiveTasks(today).size());
        Task task = tasks.getActive(details.taskIndex(), today);
        task.snoozeUntil(details.until());
        storage.save(tasks.asList());
        return "Snoozed. Future you can handle this:" + System.lineSeparator()
                + "  " + task + " (snoozed until: " + details.until().format(DISPLAY_DATE_FORMAT) + ")";
    }

    private String formatTaskCount() {
        String taskWord = tasks.size() == 1 ? "task" : "tasks";
        return "You're juggling " + tasks.size() + " " + taskWord + " now.";
    }

    private void execute(String command, CommandType commandType)
            throws TwizzyException, IOException {
        switch (commandType) {
        case HELP:
            ui.showHelp();
            break;
        case FIND:
            findTasks(command);
            break;
        case LIST:
            if (command.equals("list snoozed")) {
                ui.showSnoozedTasks(tasks.getSnoozedTasks(LocalDate.now()));
            } else {
                ui.showTaskList(tasks, LocalDate.now());
            }
            break;
        case MARK:
            changeTaskStatus(command, commandType, true);
            break;
        case UNMARK:
            changeTaskStatus(command, commandType, false);
            break;
        case DELETE:
            deleteTask(command, commandType);
            break;
        case SNOOZE:
            snoozeTask(command);
            break;
        case TODO:
        case DEADLINE:
        case EVENT:
        case UNKNOWN:
            addTask(command, commandType);
            break;
        case BYE:
            break;
        }
    }

    private void findTasks(String command) throws TwizzyException {
        String keyword = parser.parseFindKeyword(command);
        ui.showMatchingTasks(tasks.findActiveTasks(keyword, LocalDate.now()));
    }

    private void addTask(String command, CommandType commandType)
            throws TwizzyException, IOException {
        Task task = parser.parseTask(command, commandType);
        tasks.add(task);
        storage.save(tasks.asList());
        ui.showTaskAdded(task, tasks.size());
    }

    private void changeTaskStatus(String command, CommandType commandType, boolean isDone)
            throws TwizzyException, IOException {
        LocalDate today = LocalDate.now();
        int taskIndex = parser.parseTaskIndex(command, commandType, tasks.getActiveTasks(today).size());
        Task task = tasks.getActive(taskIndex, today);
        if (task.isDone() == isDone) {
            String status = isDone ? "done" : "not done";
            throw new TwizzyException("That task is already marked as " + status + ".");
        }
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        storage.save(tasks.asList());
        if (isDone) {
            ui.showTaskMarked(task);
        } else {
            ui.showTaskUnmarked(task);
        }
    }

    private void deleteTask(String command, CommandType commandType)
            throws TwizzyException, IOException {
        LocalDate today = LocalDate.now();
        int taskIndex = parser.parseTaskIndex(command, commandType, tasks.getActiveTasks(today).size());
        Task removedTask = tasks.removeActive(taskIndex, today);
        storage.save(tasks.asList());
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    private void snoozeTask(String command) throws TwizzyException, IOException {
        LocalDate today = LocalDate.now();
        Parser.SnoozeDetails details = parser.parseSnooze(command, tasks.getActiveTasks(today).size());
        Task task = tasks.getActive(details.taskIndex(), today);
        task.snoozeUntil(details.until());
        storage.save(tasks.asList());
        ui.showTaskSnoozed(task);
    }
}
