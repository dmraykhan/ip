import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves Twizzy tasks in a human-readable text file.
 */
public class Storage {
    private static final String FIELD_SEPARATOR = " | ";

    private final Path filePath;

    /**
     * Creates storage that reads from and writes to the given path.
     *
     * @param filePath location of the task data file
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all saved tasks, returning an empty list when no data file exists yet.
     *
     * @return tasks reconstructed from the data file
     * @throws IOException if the file cannot be read or contains invalid data
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(filePath);
        for (int i = 0; i < lines.size(); i++) {
            try {
                tasks.add(parseTask(lines.get(i)));
            } catch (IllegalArgumentException exception) {
                throw new IOException("Invalid task data on line " + (i + 1) + ".", exception);
            }
        }
        return tasks;
    }

    /**
     * Replaces the data file contents with the current task list.
     *
     * @param tasks tasks to persist
     * @throws IOException if the directory or file cannot be written
     */
    public void save(List<Task> tasks) throws IOException {
        Path parentDirectory = filePath.getParent();
        if (parentDirectory != null) {
            Files.createDirectories(parentDirectory);
        }

        ArrayList<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatTask(task));
        }
        Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Deadline deadline) {
            return "D" + FIELD_SEPARATOR + status + FIELD_SEPARATOR
                    + escape(deadline.getDescription()) + FIELD_SEPARATOR + deadline.getBy();
        }
        if (task instanceof Event event) {
            return "E" + FIELD_SEPARATOR + status + FIELD_SEPARATOR
                    + escape(event.getDescription()) + FIELD_SEPARATOR
                    + event.getFrom() + FIELD_SEPARATOR + event.getTo();
        }
        return "T" + FIELD_SEPARATOR + status + FIELD_SEPARATOR + escape(task.getDescription());
    }

    private Task parseTask(String line) {
        List<String> fields = splitFields(line);
        if (fields.size() < 3) {
            throw new IllegalArgumentException("Too few fields");
        }

        Task task;
        switch (fields.get(0)) {
        case "T":
            requireFieldCount(fields, 3);
            task = new Todo(fields.get(2));
            break;
        case "D":
            requireFieldCount(fields, 4);
            task = new Deadline(fields.get(2), LocalDate.parse(fields.get(3)));
            break;
        case "E":
            requireFieldCount(fields, 5);
            task = new Event(fields.get(2), LocalDate.parse(fields.get(3)),
                    LocalDate.parse(fields.get(4)));
            break;
        default:
            throw new IllegalArgumentException("Unknown task type");
        }

        if (fields.get(1).equals("1")) {
            task.markAsDone();
        } else if (!fields.get(1).equals("0")) {
            throw new IllegalArgumentException("Invalid task status");
        }
        return task;
    }

    private void requireFieldCount(List<String> fields, int expectedCount) {
        if (fields.size() != expectedCount) {
            throw new IllegalArgumentException("Unexpected number of fields");
        }
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\|");
    }

    private List<String> splitFields(String line) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean isEscaped = false;

        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (isEscaped) {
                field.append(character);
                isEscaped = false;
            } else if (character == '\\') {
                isEscaped = true;
            } else if (character == '|') {
                fields.add(field.toString().trim());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        if (isEscaped) {
            throw new IllegalArgumentException("Incomplete escape sequence");
        }
        fields.add(field.toString().trim());
        return fields;
    }
}
