/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    private final String type;
    private final String by;
    private final String from;
    private final String to;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this(description, "T", null, null, null);
    }

    /**
     * Creates an incomplete task with its type-specific date or time details.
     *
     * @param description description of the task
     * @param type task type icon: T, D, or E
     * @param by deadline text, or {@code null} for non-deadlines
     * @param from event start text, or {@code null} for non-events
     * @param to event end text, or {@code null} for non-events
     */
    public Task(String description, String type, String by, String from, String to) {
        this.description = description;
        this.isDone = false;
        this.type = type;
        this.by = by;
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the icon used to display the task's completion status.
     *
     * @return {@code X} if completed, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /** Marks this task as completed. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as not completed. */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Formats this task with its status icon and description.
     *
     * @return display form of this task
     */
    @Override
    public String toString() {
        String details = "";
        if (by != null) {
            details = " (by: " + by + ")";
        } else if (from != null && to != null) {
            details = " (from: " + from + " to: " + to + ")";
        }
        return "[" + type + "][" + getStatusIcon() + "] " + description + details;
    }
}
