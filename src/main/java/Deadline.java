/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description description of the task
     * @param by deadline text supplied by the user
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the saved deadline text.
     *
     * @return deadline text supplied by the user
     */
    public String getBy() {
        return by;
    }

    /**
     * Formats this task with its deadline details.
     *
     * @return display form of this deadline
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
