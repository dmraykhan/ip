import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/** Coordinates Twizzy's UI, parser, task list, and storage components. */
public class Twizzy {
    private static final Path DATA_FILE_PATH = Path.of("data", "twizzy.txt");

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

    private void loadTasks() {
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException exception) {
            tasks = new TaskList(List.of());
            ui.showError("I couldn't load saved tasks: " + exception.getMessage());
            ui.showDivider();
        }
    }

    private void execute(String command, CommandType commandType)
            throws TwizzyException, IOException {
        switch (commandType) {
        case LIST:
            ui.showTaskList(tasks);
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

    private void addTask(String command, CommandType commandType)
            throws TwizzyException, IOException {
        Task task = parser.parseTask(command, commandType);
        tasks.add(task);
        storage.save(tasks.asList());
        ui.showTaskAdded(task, tasks.size());
    }

    private void changeTaskStatus(String command, CommandType commandType, boolean isDone)
            throws TwizzyException, IOException {
        int taskIndex = parser.parseTaskIndex(command, commandType, tasks.size());
        Task task = tasks.get(taskIndex);
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
        int taskIndex = parser.parseTaskIndex(command, commandType, tasks.size());
        Task removedTask = tasks.remove(taskIndex);
        storage.save(tasks.asList());
        ui.showTaskDeleted(removedTask, tasks.size());
    }
}
